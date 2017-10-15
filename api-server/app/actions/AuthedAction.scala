package actions

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.Results

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import pdi.jwt._
import pdi.jwt.JwtSession
import pdi.jwt.JwtSession._

import models.Session
import core.ResponseException

class SessionRequest[A](val authInfo: Session, request: Request[A]) extends WrappedRequest[A](request)

class AuthedAction @Inject()(parser: BodyParsers.Default)(implicit val ec: ExecutionContext) 
  extends ActionBuilder[SessionRequest, AnyContent] {

  def executionContext = ec
  def parser           = parser

  override def invokeBlock[A](request: Request[A], block: (SessionRequest[A]) => Future[Result]) = {
    request.jwtSession.getAs[Session]("session")
    .map((x) => block(new SessionRequest(x, request)))
    .getOrElse({
      throw new ResponseException(message = "Unauthorize Request", status = Results.Unauthorized)
    })
  }
}