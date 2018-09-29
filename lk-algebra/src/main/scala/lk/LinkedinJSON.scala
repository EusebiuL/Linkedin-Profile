package lk

import busymachines.json.{Codec, derive}
import io.circe.{Decoder, Encoder}
import lk.entity.{AccessResponse, Profile}
import cats.implicits._

trait LinkedinJSON {

  implicit val accessTokenCirceCodec: Codec[AccessToken] = Codec.instance[AccessToken](
    encode = Encoder.apply[String].contramap(AccessToken.unapply),
    decode = Decoder.apply[String].map(AccessToken.apply)
  )

  implicit val firstNameCirceCodec: Codec[FirstName] = Codec.instance[FirstName](
    encode = Encoder.apply[String].contramap(FirstName.unapply),
    decode = Decoder.apply[String].map(FirstName.apply)
  )

  implicit val lastNameCirceCodec: Codec[LastName] = Codec.instance[LastName](
    encode = Encoder.apply[String].contramap(LastName.unapply),
    decode = Decoder.apply[String].map(LastName.apply)
  )

  implicit val picUrlCirceCodec: Codec[PicURL] = Codec.instance[PicURL](
    encode = Encoder.apply[String].contramap(PicURL.unapply),
    decode = Decoder.apply[String].map(PicURL.apply)
  )

  implicit val headlineCirceCodec: Codec[Headline] = Codec.instance[Headline](
    encode = Encoder.apply[String].contramap(PicURL.unapply),
    decode = Decoder.apply[String].map(PicURL.apply)
  )

  implicit val accessResponseCirceCodec: Codec[AccessResponse]  = derive.codec[AccessResponse]

  implicit val profileCirceCodec:        Codec[Profile] = derive.codec[Profile]

}
