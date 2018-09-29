package lk.entity

import lk.AccessToken

final case class AccessResponse(
    access_token: AccessToken,
    expires_in: Long
) extends Serializable
