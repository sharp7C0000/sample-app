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
import models.User

@Singleton
class AppController @Inject()(jsonAction: JsonAction,  ws: WSClient, ec: ExecutionContext, cc: ControllerComponents) extends AbstractController(cc) {
  
  implicit val iEc = ec

  val KEY = ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")
  
  def current() = jsonAction.async { implicit request =>

    val sessionInfo = request.jwtSession.getAs[Session]("session")

    sessionInfo.map(x => {
      ws.url("https://api.twitter.com/1.1/users/show.json")
      .addQueryStringParameters("user_id" -> x.id)
      .sign(OAuthCalculator(KEY, RequestToken(x.accessToken, x.tokenSecret)))
      .get
      .map(result => {
        if(result.status == 200) {
          val name     = result.json("name").as[String]
          val photoSrc = result.json("profile_image_url").as[String]
          val id       = result.json("id_str").as[String]

          Ok(Json.toJson(User(id, name, photoSrc)))
        } else {
          throw new Exception("Server Error")
        }
      })
      .recover {
        case errors: Exception => BadRequest(Json.obj("message" -> errors.getMessage))
      }
    })
    .getOrElse({
      throw new Exception("No Session")
    })
  }
}