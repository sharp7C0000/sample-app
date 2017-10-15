package actions

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.Results

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.functional.syntax._

import core.ResponseException
import models.Session

class JsonAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    try {
      block(request)
    }
    catch {
      case e: ResponseException => {
        Future.successful(e.status(Json.obj("message" -> e.getMessage)))
      }
      case e: Exception         => {
        play.Logger.debug(e.getMessage)
        Future.successful(Results.BadRequest(Json.obj("message" -> "Unexpected server error")))
      }
    }
  }
}