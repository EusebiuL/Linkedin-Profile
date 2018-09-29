package lk.server

import cats.effect.{Concurrent, Effect, IO}
import fs2.StreamApp
import monix.execution.Scheduler
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import fs2.Stream
import scala.concurrent.ExecutionContext

object LinkedInServerApp extends StreamApp[IO] {

  override def stream(
      args: List[String],
      requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] = {
    implicit val sch: Scheduler = Scheduler.global
    for {
      server <- LinkedinServer.concurrent[IO]
      (serverConfig, lkModule) <- server.init
      exitCode <- serverStream[IO](
        config = serverConfig,
        service = lkModule.lkServerService
      )
    } yield exitCode
  }

  private def serverStream[F[_]: Effect: Concurrent](
      config: LinkedInServerConfig,
      service: HttpService[F]
  )(implicit ec: ExecutionContext): Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
      .bindHttp(config.port, config.host)
      .mountService(service, config.apiRoot)
      .serve

}
