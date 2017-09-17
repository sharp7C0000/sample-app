package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import play.api.libs.json.Json
import play.api.libs.ws._
import play.api.libs.oauth.OAuthCalculator

import play.api.libs.oauth._
import play.api.libs.oauth.OAuth._

import pdi.jwt._
import pdi.jwt.JwtSession
import pdi.jwt.JwtSession._

import models.Session

@Singleton
class AuthController @Inject()(ws: WSClient, ec: ExecutionContext, cc: ControllerComponents) extends AbstractController(cc) {

  implicit val iEc = ec

  val KEY = ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")

  val oauth = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    true)

  def authorize = Action.async { implicit request: Request[AnyContent] =>

    val body     = request.body
    val jsonBody = body.asJson

    jsonBody.map { json =>

      val token  = json("oauthToken").as[String]
      val secret = json("oauthSecret").as[String]

      ws.url("https://api.twitter.com/1.1/account/verify_credentials.json")
      .addQueryStringParameters("include_email" -> "false")
      .sign(OAuthCalculator(KEY, RequestToken(token, secret)))
      .get
      .map(result => {

        var session = JwtSession()
        val s       = Session(result.json("id_str").as[String], token, secret)
        session = session + ("session", s)

        Ok(session.serialize)
      })
    }.getOrElse {
      Future.successful(BadRequest("Expecting application/json request body"))
    }
  }

  def authTwitter() = Action { implicit request: Request[AnyContent] =>
    oauth.retrieveRequestToken(routes.AuthController.authTwitterCallback.absoluteURL()) match {
      case Right(t) => {
        Redirect(oauth.redirectUrl(t.token))
      }
      case Left(e) => throw e
    }
  }

  def authTwitterCallback() = Action { implicit request: Request[AnyContent] =>

    val oauthVerifier = request.getQueryString("oauth_verifier").mkString
    val oauthToken    = request.getQueryString("oauth_token").mkString

    oauth.retrieveAccessToken(RequestToken(oauthToken, ""), oauthVerifier) match {
      case Right(t) => {
        Redirect(s"http://localhost/login?oauth_token=${t.token}&oauth_secret=${t.secret}")
      }
      case Left(e) => throw e
    }
  }

  

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