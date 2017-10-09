package actions

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

import models.Session

case class JsonIng[A](action: Action[A]) extends Action[A] with Results {

  def apply(request: Request[A]): Future[Result] = {
    try {
      action(request)
    }
    catch {
      case e: Exception => Future.successful(BadRequest(Json.obj("message" -> e.getMessage)))
    }
  }

  override def parser           = action.parser
  override def executionContext = action.executionContext
}

class JsonAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    block(request)
  }
  override def composeAction[A](action: Action[A]) = new JsonIng(action)
}