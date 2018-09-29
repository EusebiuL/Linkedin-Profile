package lk.server

import cats.effect.Sync
import lk.config.ConfigLoader

final case class LinkedInServerConfig(port: Int, host: String, apiRoot: String)

object LinkedInServerConfig extends ConfigLoader[LinkedInServerConfig] {

  override def default[F[_]: Sync]: F[LinkedInServerConfig] =
    this.load[F]("lk.server")
}
