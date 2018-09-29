package lk.core

import cats.effect.Async
import io.chrisdavenport.linebacker.DualContext

trait BlockingAlgebra[F[_]] {

  protected def block[M](thunk: => F[M])(implicit F: Async[F], dualContext: DualContext[F]): F[M] = {
    dualContext.block(thunk)
  }
}
