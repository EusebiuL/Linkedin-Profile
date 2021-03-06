package lk.json

import cats.Applicative
import cats.effect.Sync
import fs2.Chunk
import io.circe.Json
import org.http4s.MediaType
import org.http4s.headers.`Content-Type`
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.CirceInstances
import io.circe.{Decoder, Encoder, Printer}
import cats.implicits._

trait Http4sCirceInstances {
  import Http4sCirceInstances._
  /**
    * This code was copied from [[org.http4s.circe.CirceInstances#jsonEncoderWithPrinter]]
    * Ideally, we would have done directly:
    * {{{
    *   circeInstance.jsonEncoderOf[F, T]
    * }}}
    * But that throws us into an infinit loop because the implicit picks itself up.
    * @return
    */
  implicit def syncEntityJsonEncoder[F[_]: Applicative, T: Encoder]: EntityEncoder[F, T] =
    EntityEncoder[F, Chunk[Byte]]
      .contramap[Json] { json =>
      val bytes = printer.prettyByteBuffer(json)
      Chunk.byteBuffer(bytes)
    }
      .withContentType(`Content-Type`(MediaType.`application/json`))
      .contramap(t => Encoder[T].apply(t))
  implicit def syncEntityJsonDecoder[F[_]: Sync, T: Decoder]: EntityDecoder[F, T] =
    circeInstances.jsonOf[F, T]
}
object Http4sCirceInstances {
  private val printer        = Printer.noSpaces.copy(dropNullValues = true)
  private val circeInstances = CirceInstances.withPrinter(printer)
}
