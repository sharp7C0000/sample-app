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

  def fromTwitter() = Action { implicit request: Request[AnyContent] =>
    oauth.retrieveRequestToken("http://localhost/page1") match {
      case Right(t) => {
        Redirect(oauth.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
      }
      case Left(e) => throw e
    }
  }

  def twitterSignIn(oauthToken: String, oauthVerifier: String) = Action.async { implicit request: Request[AnyContent] =>

    //val verifier = request.getQueryString("oauth_verifier")

    def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
      for {
        token  <- request.session.get("token")
        secret <- request.session.get("secret")
      } yield {
        RequestToken(token, secret)
      }
    }

    val tokenPair = sessionTokenPair(request).get
      
    oauth.retrieveAccessToken(tokenPair, oauthVerifier) match {
      case Right(t) => {
        ws.url("https://api.twitter.com/1.1/account/verify_credentials.json")
        .addQueryStringParameters("include_email" -> "false")
        .sign(OAuthCalculator(KEY, t))
        .get
        .map(result => {
          val session = Session(result.json("id_str").as[String], t.token, t.secret)
          Ok("success").withNewSession.addingToJwtSession("Session", session)
        })
      }
      case Left(e) => throw e
    }
  }
}