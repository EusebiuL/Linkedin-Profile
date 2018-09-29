package lk.config

import cats.effect.Sync
import pureconfig._
import pureconfig.error.ConfigReaderFailures

trait ConfigLoader[Config] {

  def default[F[_]:Sync] :F[Config]

  def load[F[_]:Sync](implicit reader : Derivation[ConfigReader[Config]]) : F[Config] = {
    suspendInF(pureconfig.loadConfig[Config])
  }

  def load[F[_]:Sync](namespace:String)(implicit reader: Derivation[ConfigReader[Config]]) : F[Config] = {
    suspendInF(pureconfig.loadConfig[Config](namespace))
  }

  private def suspendInF[F[_]:Sync](thunk: => Either[ConfigReaderFailures, Config]): F[Config] = {

    val F = Sync.apply[F]
    F.flatMap(F.delay(thunk)){
      case Left(err:ConfigReaderFailures) => F.raiseError(ConfigReadingAnomalies(err))
      case Right(c) => F.pure(c)
    }

  }
}
