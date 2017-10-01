package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.functional.syntax._

import pdi.jwt._
import pdi.jwt.JwtSession
import pdi.jwt.JwtSession._

import actions.JsonAction

@Singleton
class AppController @Inject()(jsonAction: JsonAction,  cc: ControllerComponents) extends AbstractController(cc) {
  def current() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj("message" -> "bar"))
  }
}