package lk

import cats.effect.Effect


trait ModuleLinkedinEffect[F[_]] {

  implicit def effect: Effect[F]

  implicit def linkedinContext: LinkedinContext[F]

  def linkedinConfig: LinkedinConfig

  def linkedinAlgebra: LinkedinAlgebra[F] = _linkedinAlgebra

  private lazy val _linkedinAlgebra = new impl.AsyncAlgebraImpl[F](linkedinConfig)

}
