package lk.server

import cats.data.NonEmptyList
import cats.effect.{Concurrent, ConcurrentEffect, Effect}
import lk.service.ModuleLinkedinOrganizerRestAsync
import lk.{LinkedinConfig, LinkedinContext, ModuleLinkedinEffect}
import org.http4s.HttpService

trait ModuleLinkedinServer[F[_]] extends ModuleLinkedinEffect[F] with ModuleLinkedinOrganizerRestAsync[F]{

  implicit override def effect: Effect[F]

  override def linkedinConfig: LinkedinConfig

  implicit override def linkedinContext: LinkedinContext[F]

  /*_*/
  def lkServerService: HttpService[F] = {
    import cats.implicits._

    val service: HttpService[F] = {
      NonEmptyList
        .of[HttpService[F]](
          linkedinModuleService
        )
        .reduceK
    }
  }
}

object ModuleLinkedinServer {

  def concurrent[F[_]](
      linkedConfig: LinkedinConfig,
  )(
      implicit c: ConcurrentEffect[F],
      lctx: LinkedinContext[F]
  ): ModuleLinkedinServer[F] = {
    new ModuleLinkedinServer[F] {

      implicit override def concurrent: Concurrent[F] = c

      implicit override def effect: Effect[F] = c

      override def linkedinConfig: LinkedinConfig = linkedConfig

      implicit override def linkedinContext: LinkedinContext[F] = lctx

    }
  }
}
