package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.api.libs.ws._
import play.api.libs.oauth.OAuthCalculator

import play.api.libs.oauth._
import play.api.libs.oauth.OAuth._

import pdi.jwt._
import pdi.jwt.JwtSession
import pdi.jwt.JwtSession._

import actions.JsonAction
import models.Session



@Singleton
class AuthController @Inject()(jsonAction: JsonAction, ws: WSClient, ec: ExecutionContext, cc: ControllerComponents) extends AbstractController(cc) {

  implicit val iEc = ec

  case class OAuthTokenPair(oauthToken: String, oauthSecret: String)
  implicit val oAuthTokenPairFormat = Json.format[OAuthTokenPair]

  val KEY = ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")

  val oauth = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    true)

  def authorize = jsonAction.async(parse.json) { implicit request =>
    request.body.validate[OAuthTokenPair].fold(
      errors   => throw new Exception("Invalid body parameter"),
      authInfo => {
        ws.url("https://api.twitter.com/1.1/account/verify_credentials.json")
        .addQueryStringParameters("include_email" -> "false")
        .sign(OAuthCalculator(KEY, RequestToken(authInfo.oauthToken, authInfo.oauthSecret)))
        .get
        .map(result => {
          if(result.status == 200) {
            var session = JwtSession()
            val s       = Session(result.json("id_str").as[String], authInfo.oauthToken, authInfo.oauthSecret)
            session = session + ("session", s)
            Ok(session.serialize)
          } else {
            BadRequest(Json.obj("message" -> "Auth credential fail"))
          }
        })
        .recover {
          case errors: Exception => BadRequest(Json.obj("message" -> errors.getMessage))
        }
      }
    )
  }

  def authTwitter(callbackUrl: String) = Action { implicit request: Request[AnyContent] =>
    oauth.retrieveRequestToken(routes.AuthController.authTwitterCallback.absoluteURL() + s"?callback_url=${callbackUrl}") match {
      case Right(t) => {
        Redirect(oauth.redirectUrl(t.token))
      }
      case Left(e) => throw e
    }
  }

  def authTwitterCallback() = Action { implicit request: Request[AnyContent] =>

    val oauthVerifier = request.getQueryString("oauth_verifier").mkString
    val oauthToken    = request.getQueryString("oauth_token").mkString
    val callbackUrl   = request.getQueryString("callback_url").mkString

    oauth.retrieveAccessToken(RequestToken(oauthToken, ""), oauthVerifier) match {
      case Right(t) => {
        Redirect(s"${callbackUrl}?oauth_token=${t.token}&oauth_secret=${t.secret}")
      }
      case Left(e) => throw e
    }
  }








  // old code

  def fromTwitter() = Action { implicit request: Request[AnyContent] =>
    oauth.retrieveRequestToken("http://localhost/page1") match {
      case Right(t) => {
        Redirect(oauth.redirectUrl(t.token))
      }
      case Left(e) => throw e
    }
  }

  def twitterSignIn(oauthToken: String, oauthVerifier: String) = Action.async { implicit request: Request[AnyContent] =>
    oauth.retrieveAccessToken(RequestToken(oauthToken, ""), oauthVerifier) match {
      case Right(t) => {
        ws.url("https://api.twitter.com/1.1/account/verify_credentials.json")
        .addQueryStringParameters("include_email" -> "false")
        .sign(OAuthCalculator(KEY, t))
        .get
        .map(result => {
          val session = Session(result.json("id_str").as[String], t.token, t.secret)
          Ok("success").addingToJwtSession("Session", session)
        })
      }
      case Left(e) => throw e
    }
  }
}