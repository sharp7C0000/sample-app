package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.libs.ws._

import play.api.libs.oauth._
import play.api.libs.oauth.OAuth._

import pdi.jwt._
import pdi.jwt.JwtSession
import pdi.jwt.JwtSession._

import play.api.libs.json.Json

import play.api.libs.oauth.OAuthCalculator

@Singleton
class AuthController @Inject()(ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) {

  def fromTwitter() = Action { implicit request: Request[AnyContent] =>

    val KEY = ConsumerKey("bGKjn0l5Zu92zTBRruN1U8YCo", "GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G")

    val oauth = OAuth(ServiceInfo(
      "https://api.twitter.com/oauth/request_token",
      "https://api.twitter.com/oauth/access_token",
      "https://api.twitter.com/oauth/authorize", KEY),
      true)

    request.queryString.map { case (k,v) => k -> play.api.Logger.debug(v.mkString) };

    def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
      for {
        token  <- request.session.get("token")
        secret <- request.session.get("secret")
      } yield {
        RequestToken(token, secret)
      }
    }

    Ok("foo")


    // request.getQueryString("oauth_verifier").map { verifier =>
    //   try {
    //     val tokenPair   = sessionTokenPair(request).get
    //     val accessToken = oauth.retrieveAccessToken(tokenPair, verifier)
        
    //     // Ok("Log in success").withNewSession.addingToJwtSession(Json.obj(
    //     //   "token"  -> tokenPair.token,
    //     //   "secret" -> tokenPair.secret,
    //     // ))


    //     ws.url("https://api.twitter.com/1.1/users/show.json")
    //     .sign(OAuthCalculator(KEY, tokenPair))
    //     .get
    //     .map(result => Ok(result.json))

    //     // sessionTokenPair match {
    //     //   case Some(credentials) => {
            
    //     //   }
    //     //   case _ => Future.successful(Redirect(routes.Application.authenticate))
    //     // }

    //     //play.api.Logger.debug("!!!!!!!!!!!!!! " + accessToken)
    //     //Redirect(routes.HomeController.index).addingToJwtSession(("logged", "true"))

    //      //var session = JwtSession()

    //     //Ok.addingToJwtSession(("logged", "true"))
    //     //Ok("Hello world again").withNewSession
        
    //   } catch {
    //     case e: Exception => throw e
    //   }
    // }.getOrElse(
    //   try {
    //     val requestToken = oauth.retrieveRequestToken("http://localhost:9000/auth/twitter")
    //     Redirect(oauth.redirectUrl(requestToken.token)).withSession("token" -> requestToken.token, "secret" -> requestToken.secret)
    //   } catch {
    //     case e: Exception => throw e
    //   }
    // )
  }
}