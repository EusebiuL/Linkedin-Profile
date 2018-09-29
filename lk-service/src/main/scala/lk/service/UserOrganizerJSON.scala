package lk.service

import busymachines.json.Codec
import io.circe.{Decoder, Encoder}
import lk.{FirstName, Headline, LastName, PicURL}
import cats.implicits._
import lk.entity.Profile
import lk.json._
/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 29/09/18
  *
  */

class UserOrganizerJSON {

  implicit val firstNameCirceCodec: Codec[FirstName] = Codec.instance[FirstName](
    encode = Encoder.apply[String].contramap(FirstName.unapply),
    decode = Decoder.apply[String].map(FirstName.apply)
  )

  implicit val lastNameCirceCodec: Codec[LastName] = Codec.instance[LastName](
    encode = Encoder.apply[String].contramap(LastName.unapply),
    decode = Decoder.apply[String].map(LastName.apply)
  )

  implicit val picURLCirceCodec: Codec[PicURL] = Codec.instance[PicURL](
    encode = Encoder.apply[String].contramap(PicURL.unapply),
    decode = Decoder.apply[String].map(PicURL.apply)
  )

  implicit val headlineCirceCodec: Codec[Headline] = Codec.instance[Headline](
    encode = Encoder.apply[String].contramap(Headline.unapply),
    decode = Decoder.apply[String].map(Headline.apply)
  )

  implicit val authProfileCirceCodec: Codec[Profile] = derive.codec[Profile]
}
