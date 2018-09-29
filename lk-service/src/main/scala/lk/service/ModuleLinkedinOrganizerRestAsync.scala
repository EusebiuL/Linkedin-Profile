package lk.service

import cats.data.NonEmptyList
import lk.ModuleLinkedinEffect
import org.http4s.HttpService

trait ModuleLinkedinOrganizerRestAsync[F[_]] { this: ModuleLinkedinEffect[F] =>

  def linkedinModuleService: HttpService[F] = _service

  private def linkedinOrganizer: UserLinkedinOrganizer[F] = _linkedinRestOrganizer

  private lazy val _linkedinRestOrganizer: UserLinkedinOrganizer[F] = new UserLinkedinOrganizer[F](linkedinAlgebra = linkedinAlgebra)

  private lazy val _service: HttpService[F] = {
    import cats.implicits._
    NonEmptyList
      .of(
        linkedinOrganizer.linkedinModuleService,

      )
      .reduceK
  }

}
