package lk

import cats.effect.Sync
import lk.config.ConfigLoader


final case class LinkedinConfig(
    clientId: String,
    clientSecret: String,
    scope: String,
    state: String,
    redirectUri: String
)

object LinkedinConfig extends ConfigLoader[LinkedinConfig] {
  override def default[F[_]: Sync]: F[LinkedinConfig] =
    this.load[F]("lk.linkedin")
}
