package lk.server

import cats.effect.{Concurrent, ConcurrentEffect}
import lk.{LinkedinConfig, LinkedinContext}
import monix.execution.Scheduler
import fs2.Stream
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext

final class LinkedinServer[F[_]: ConcurrentEffect] private (
    implicit scheduler: Scheduler
) {

  private val logger = Slf4jLogger.unsafeCreate[F]

  def init(implicit ec: ExecutionContext): Stream[F, (LinkedInServerConfig, ModuleLinkedinServer[F])] = {
    for {
      serverConfig    <- Stream.eval(LinkedInServerConfig.default[F])
      _               <- Stream.eval(logger.info(s"Successfully configured server"))
      linkedinConfig  <- Stream.eval(LinkedinConfig.default[F])
      linkedinContext <- LinkedinContext.create[F]
      mtwModule <- Stream.eval(
        moduleInit(
          linkedinConfig,
          linkedinContext,
        )
      )
      _ <- Stream.eval(logger.info("Successfully initialized linkedin-server"))
    } yield (serverConfig, mtwModule)
  }

  private def moduleInit(
                          linkedinConfig:  LinkedinConfig,
                          linkedinContext: LinkedinContext[F],
                        ): F[ModuleLinkedinServer[F]] =
    Concurrent
      .apply[F]
      .delay(
        ModuleLinkedinServer.concurrent(linkedinConfig)(
          implicitly,
          linkedinContext
        )
      )


}

object LinkedinServer {

  def concurrent[F[_]: ConcurrentEffect](implicit scheduler: Scheduler): Stream[F, LinkedinServer[F]] =
    Stream.eval(Concurrent.apply[F].delay(new LinkedinServer[F]()))

}