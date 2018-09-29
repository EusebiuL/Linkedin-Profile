package lk.core

import shapeless.tag.@@

trait PhantomType[T] {

  type Phantom <: this.type

  type Raw  = T
  type Type = T @@ Phantom

  @inline def apply(value: T): @@[T, Phantom] =
    shapeless.tag[Phantom](value)

  @inline def unapply(phantom: Type): T =
    identity(phantom)
}
