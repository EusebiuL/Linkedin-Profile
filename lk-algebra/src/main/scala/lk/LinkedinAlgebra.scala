package lk

import cats.effect.Effect
import lk.entity.{AccessResponse, Profile}
import org.http4s.Uri


trait LinkedinAlgebra[F[_]] {

  def getAuthorizationUri: F[Uri]

  def getAccessResponse(code: AuthCode, state: AuthState): F[AccessResponse]

  def getProfileForAccessToken(token: AccessToken): F[Profile]

}

object LinkedinAlgebra{

  def effect[F[_]: Effect](config: LinkedinConfig)(implicit linkedinContext: LinkedinContext[F]): LinkedinAlgebra[F] = new impl.AsyncAlgebraImpl[F](config)

}