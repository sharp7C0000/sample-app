package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.libs.oauth._
import play.libs.oauth.OAuth._

@Singleton
class AuthController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def fromTwitter() = Action { implicit request: Request[AnyContent] =>

    val KEY = new ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")

    val oauth = new OAuth(new ServiceInfo(
      "https://api.twitter.com/oauth/request_token",
      "https://api.twitter.com/oauth/access_token",
      "https://api.twitter.com/oauth/authorize", KEY),
      true)

    def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
      for {
        token  <- request.session.get("token")
        secret <- request.session.get("secret")
      } yield {
        new RequestToken(token, secret)
      }
    }

    def requestToken = {
      try {
        val requestToken = oauth.retrieveRequestToken("http://localhost:9000/auth/twitter")
        Redirect(oauth.redirectUrl(requestToken.token)).withSession("token" -> requestToken.token, "secret" -> requestToken.secret)
      } catch {
        case e: Exception => throw e
      }
    }

    def accessToken = {
      try {
        val requestToken = oauth.retrieveRequestToken("http://localhost:9000/auth/twitter")
        Redirect(oauth.redirectUrl(requestToken.token)).withSession("token" -> requestToken.token, "secret" -> requestToken.secret)
      } catch {
        case e: Exception => throw e
      }
    }

    request.getQueryString("oauth_verifier").map { verifier =>
      try {
        val tokenPair   = sessionTokenPair(request).get
        val accessToken = oauth.retrieveAccessToken(tokenPair, verifier)
        Redirect(routes.HomeController.index).withSession("token" -> accessToken.token, "secret" -> accessToken.secret)
      } catch {
        case e: Exception => throw e
      }
    }.getOrElse(
      try {
        val requestToken = oauth.retrieveRequestToken("http://localhost:9000/auth/twitter")
        Redirect(oauth.redirectUrl(requestToken.token)).withSession("token" -> requestToken.token, "secret" -> requestToken.secret)
      } catch {
        case e: Exception => throw e
      }
    )
  }
}