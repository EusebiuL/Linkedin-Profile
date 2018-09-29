package lk.json

import busymachines.json.{JsonSyntax, SemiAutoDerivation}

object syntax extends JsonSyntax.Implicits

object derive extends SemiAutoDerivation

object autoderive extends io.circe.generic.extras.AutoDerivation
