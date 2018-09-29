package lk

import cats.effect.Effect
import lk.entity.{AccessResponse, Profile}
import org.http4s.blaze.http.Uri

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 29/09/18
  *
  */

trait LinkedinAlgebra[F[_]] {

  def getAuthorizationUri: F[Uri]

  def getAccessResponse(code: AuthCode, state: AuthState): F[AccessResponse]

  def getProfileForAccessToken(token: AccessToken): F[Profile]

}

object LinkedinAlgebra{

  def effect[F[_]: Effect](config: LinkedinConfig)(implicit linkedinContext: LinkedinContext[F]): LinkedinAlgebra[F] = new impl.AsyncAlgebraImpl[F](config)

}