package lk.impl

import cats.implicits._
import busymachines.core.UnauthorizedFailure
import cats.effect.Effect
import org.http4s.{AuthScheme, Credentials, Status, Uri, UrlForm}
import lk._
import lk.core.BlockingAlgebra
import lk.entity.{AccessResponse, Profile}
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.headers.Authorization
import org.http4s.dsl.io._

final private[lk] class AsyncAlgebraImpl[F[_]](config: LinkedinConfig)(
  implicit
  val F:               Effect[F],
  val linkedinContext: LinkedinContext[F]
) extends LinkedinAlgebra[F] with Http4sClientDsl[F] with LinkedinJSON with BlockingAlgebra[F]{

  private lazy val _authorizationURI = F.delay(

      Uri
        .unsafeFromString("https://www.linkedin.com/oauth/v2/authorization")
        .withQueryParam("response_type", "code")
        .withQueryParam("client_id", config.clientId)
        .withQueryParam("redirect_uri", config.redirectUri)
        .withQueryParam("state", config.state)
        .withQueryParam("scope", config.scope)

  )

  private lazy val _httpClient = Http1Client[F]()

  override def getAuthorizationUri: F[Uri] = _authorizationURI

  override def getAccessResponse(code: AuthCode, state: AuthState): F[AccessResponse] = {
    for {
      httpClient <- _httpClient
      _ <- if (state == config.state) F.unit
      else F.raiseError[Unit](UnauthorizedFailure("CSRF state does not match the registered value"))
      post <- POST(
        uri("https://www.linkedin.com/oauth/v2/accessToken"),
        UrlForm(
          "grant_type"    -> "authorization_code",
          "code"          -> code,
          "redirect_uri"  -> config.redirectUri,
          "client_id"     -> config.clientId,
          "client_secret" -> config.clientSecret
        )
      )
      resp <- block {
        httpClient.fetch[AccessResponse](post) {
          case Status.Successful(r) => r.as[AccessResponse]
          case _ =>
            F.raiseError[AccessResponse](
              UnauthorizedFailure("Failed to get access token from linkedin.")
            )
        }
      }
    } yield resp
  }

  override def getProfileForAccessToken(token: AccessToken): F[Profile] = {
    for {
      httpClient <- _httpClient
      getProfile <- GET(
        uri(
          "https://api.linkedin.com/v1/people/~:(first-name,headline,last-name,picture-url)?format=json"
        ),
        Authorization(Credentials.Token(AuthScheme.Bearer, token))
      )
      profile <- block {
        httpClient.fetch[Profile](getProfile) {
          case Status.Successful(r) => r.as[Profile]
          case _ =>
            F.raiseError[Profile](UnauthorizedFailure("Failed to load profile from linkedin."))
        }
      }

    } yield profile
  }
}
