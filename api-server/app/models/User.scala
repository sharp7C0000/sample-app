package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(id: String, name: String, profileSrc: String)

object User {
  implicit val userFormat = Json.format[User]
}