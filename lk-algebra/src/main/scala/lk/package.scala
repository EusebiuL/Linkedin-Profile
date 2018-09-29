import java.time.LocalDateTime

import lk.core.PhantomType
import org.http4s.blaze.http.Uri

package object lk {

  object AccessToken extends PhantomType[String]
  type AccessToken = AccessToken.Type

  object TokenExpirationDate extends PhantomType[LocalDateTime]
  type TokenExpirationDate = TokenExpirationDate.Type

  object FirstName extends PhantomType[String]
  type FirstName = FirstName.Type

  object LastName extends PhantomType[String]
  type LastName = LastName.Type

  object PicURL extends PhantomType[String]
  type PicURL = PicURL.Type

  object AuthUri extends PhantomType[Uri]
  type AuthUri = AuthUri.Type

  object AuthCode extends PhantomType[String]
  type AuthCode = AuthCode.Type

  object AuthState extends PhantomType[String]
  type AuthState = AuthState.Type

  object Headline extends PhantomType[String]
  type Headline = AuthState.Type


}
