package lk.entity

import lk.{FirstName, Headline, LastName, PicURL}

final case class Profile(
    firstName: FirstName,
    headline: Headline,
    lastName: LastName,
    picUrl: PicURL
) extends Serializable
