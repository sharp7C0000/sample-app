package core

import play.api.mvc.Results.Status

case class ResponseException(
  val message: String = "",
  val status : Status,
  val cause  : Throwable = None.orNull) extends Exception(message, cause)