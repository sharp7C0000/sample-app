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

import play.api.mvc.Results

import core.ResponseException

class MongoLabService @Inject()(ws: WSClient, ec: ExecutionContext) {

  def call(url: String, method: String, queryParams: Seq[(String, String)] = Seq())(implicit ec: ExecutionContext) = {
    
    play.Logger.debug(s"Call MongoLab api ${url} ${method}")

    val finalQueryParams = Seq("apiKey" -> MongoLabService.API_KEY) ++ queryParams.toSeq
    
    ws.url(url)
    .withQueryStringParameters(finalQueryParams: _*)
    .execute(method)
    .map(result => {

      play.Logger.debug(s"MongoLab service result ${result}")

      result.status match {
        case 200 => {
          result
        }
        case _ => {
          throw new ResponseException("Server Error", Results.InternalServerError)
        }
      }
    })
    .recover {
      case errors: Exception => throw new ResponseException(errors.getMessage, Results.BadRequest)
    }
  }
}

object MongoLabService {
  val API_KEY = "OglJsHtl1q9GVQBtxdxq6B9kItWu9ynF"
  val DB_NAME = "gallery-meh-dev"
}