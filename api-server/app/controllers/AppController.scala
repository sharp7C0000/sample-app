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

  def current = (jsonAction andThen authedAction).async { implicit request =>

    //Future.successful(Ok)

    implicit val session = request.authInfo

    tApi.call(url = "https://api.twitter.com/1.1/users/show.json", method = "get")
    .map { resp =>
      Ok(resp.json("name").as[String].mkString)
    }
    
  }
}