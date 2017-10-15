package service

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

class TwitterService @Inject()(ws: WSClient, ec: ExecutionContext) {

  def call(url: String, method: String, queryParams: Seq[(String, String)] = Seq())(implicit session: Session, ec: ExecutionContext) = {
    ws.url(url)
    .withQueryStringParameters(queryParams.toSeq: _*)
    .sign(OAuthCalculator(TwitterService.KEY, RequestToken(session.accessToken, session.tokenSecret)))
    .execute(method)
    .map(result => {
      result.status match {
        case 200 => {
          result
        }

        case 401 => {
          throw new Exception("Authrization Required")
        }

        case _ => {
          throw new Exception("Server Error")
        }
      }
    })
    .recover {
      case errors: Exception => throw new Exception("Fuck You")//BadRequest(Json.obj("message" -> errors.getMessage))
    }
  }
}

object TwitterService {
  val KEY = ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")
}