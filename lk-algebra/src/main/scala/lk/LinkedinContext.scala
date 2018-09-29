package lk

import cats.effect.{Async, Sync}
import cats.implicits._
import io.chrisdavenport.linebacker.DualContext
import io.chrisdavenport.linebacker.contexts.Executors
import fs2.Stream
import scala.concurrent.ExecutionContext


trait LinkedinContext[F[_]] extends DualContext[F]

object LinkedinContext {

  def create[F[_]: Sync: Async](implicit ec: ExecutionContext): Stream[F, LinkedinContext[F]] =
    Executors
      .unbound[F]
      .map(
        blockingExecutor =>
          new LinkedinContext[F] {
            override def blockingContext: ExecutionContext = ec

            override def defaultContext: ExecutionContext = ExecutionContext.fromExecutorService(blockingExecutor)
          }
      )
}