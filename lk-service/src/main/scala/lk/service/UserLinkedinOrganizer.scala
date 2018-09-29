package lk.service

import busymachines.core.UnauthorizedFailure
import cats.effect.Async
import lk.entity.AccessResponse
import lk.{AuthCode, AuthState, LinkedinAlgebra}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import cats.implicits._
import org.http4s.headers.Location

final class UserLinkedinOrganizer[F[_]](
    private val linkedinAlgebra: LinkedinAlgebra[F]
)(
    implicit val F: Async[F],
) extends Http4sDsl[F]
    with UserOrganizerJSON {

  private object AuthorizationCodeMatcher
      extends OptionalQueryParamDecoderMatcher[String]("code")

  private object ErrorCodeMatcher
      extends OptionalQueryParamDecoderMatcher[String]("error")

  private object StateMatcher extends QueryParamDecoderMatcher[String]("state")

  val linkedinModuleService: HttpService[F] = HttpService[F] {
    case GET -> Root / "user" / "authorization" =>
      for {
        uri <- linkedinAlgebra.getAuthorizationUri
        redirect <- TemporaryRedirect(Location(uri))
      } yield redirect

    case GET -> Root / "user" / "authorization" / "callback" :? AuthorizationCodeMatcher(
          code)
          +& ErrorCodeMatcher(error)
          +& StateMatcher(state) =>
      for {
        accessResponse <- code match {
          case Some(c) =>
            linkedinAlgebra.getAccessResponse(AuthCode(c), AuthState(state))
          case None =>
            F.raiseError[AccessResponse](UnauthorizedFailure(error.get))
        }
        authProfile <- linkedinAlgebra.getProfileForAccessToken(
          accessResponse.access_token)
        resp <- Ok(authProfile)
      } yield resp
  }
}
