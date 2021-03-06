package controllers

import play.api.Configuration

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

import scala.concurrent.duration._

import service.MongoLabService

@Singleton
class AuthController @Inject()(jsonAction: JsonAction, ws: WSClient, ec: ExecutionContext, cc: ControllerComponents, config: Configuration, dbService: MongoLabService) extends AbstractController(cc) {

  implicit val iEc = ec

  case class OAuthTokenPair(oauthToken: String, oauthSecret: String)
  implicit val oAuthTokenPairFormat = Json.format[OAuthTokenPair]

  val KEY = ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")

  val oauth = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    true)

  /**
    트위터 인증
  */
  def authTwitter(callbackUrl: String) = Action { implicit request: Request[AnyContent] =>
    oauth.retrieveRequestToken(routes.AuthController.authTwitterCallback.absoluteURL() + s"?callback_url=${callbackUrl}") match {
      case Right(t) => {
        Redirect(oauth.redirectUrl(t.token))
      }
      case Left(e) => throw e
    }
  }

  /**
    트위터 인증 callback
  */
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

  /**
    어플리케이션 로그인
    @return JSON
  */
  def authorize = jsonAction.async(parse.json) { implicit request =>
  play.Logger.debug(" " + request.body)
    request.body.validate[OAuthTokenPair].fold(
      errors   => throw new Exception("Invalid body parameter"),
      authInfo => {
        ws.url("https://api.twitter.com/1.1/account/verify_credentials.json")
        .addQueryStringParameters("include_email" -> "false")
        .sign(OAuthCalculator(KEY, RequestToken(authInfo.oauthToken, authInfo.oauthSecret)))
        .get
        .map(result => {
          if(result.status == 200) {
            
            // db에서 회원가입여부 조회하여 유저정보 생성
            dbService.call("https://api.mlab.com/api/1/databases", "GET")
            .map { resp =>
              play.Logger.debug("!!!!" + resp)
            }

            var session = JwtSession()
            val s       = Session(result.json("id_str").as[String], authInfo.oauthToken, authInfo.oauthSecret)
            session = session + ("session", s)

            val expired = config.get[Duration]("play.http.session.maxAge").toMillis

            Ok(Json.obj("expired" -> expired, "authToken" -> session.serialize))
          } else {
            throw new Exception("Auth credential fail")
          }
        })
        .recover {
          case errors: Exception => BadRequest(Json.obj("message" -> errors.getMessage))
        }
      }
    )
  }
}