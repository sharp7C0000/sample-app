package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Session(id: String, accessToken: String, tokenSecret: String)

object Session {
  implicit val sessionFormat = Json.format[Session]
}