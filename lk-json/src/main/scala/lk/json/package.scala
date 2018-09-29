package lk

import busymachines.{ json => bmj}


package object json extends bmj.JsonTypeDefinitions with bmj.DefaultTypeDiscriminatorConfig with Http4sCirceInstances {
  type Codec[A] = bmj.Codec[A]
  @inline def Codec: bmj.Codec.type = bmj.Codec
}
