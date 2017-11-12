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
import actions.AuthedAction
import models.Session
import models.User

import service.TwitterService

@Singleton
class AppController @Inject()(authedAction: AuthedAction, jsonAction: JsonAction,  ws: WSClient, ec: ExecutionContext, cc: ControllerComponents, tApi: TwitterService) extends AbstractController(cc) {
  
  implicit val iEc = ec

  /**
    현재 세션 정보를 통해 로그인한 유저 정보 조회
    @return JSON
  */
  def current = (jsonAction andThen authedAction).async { implicit request =>
    implicit val session = request.authInfo

    tApi.call(url = "https://api.twitter.com/1.1/users/show.json", queryParams = Seq(("user_id", session.id)), method = "GET")
    .map { resp =>

      val loginUser = User(
        resp.json("id_str").as[String].mkString,
        resp.json("name").as[String].mkString,
        resp.json("profile_image_url").as[String].mkString
      )

      play.Logger.debug("Login user info", loginUser)

      Ok(Json.toJson(loginUser))
    }
  }
}