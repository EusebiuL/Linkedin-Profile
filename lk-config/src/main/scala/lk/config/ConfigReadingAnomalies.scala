package lk.config

import busymachines.core.{AnomalousFailures, AnomalyID}
import pureconfig.error.ConfigReaderFailures

final case class ConfigReadingAnomalies(cs: ConfigReaderFailures)
  extends AnomalousFailures(
    id              = ConfigReadingAnomalies.ID,
    message         = s"Failed to read config file. ${cs.toList.map(_.description).mkString(",")}",
    firstAnomaly    = ConfigReadingAnomaly(cs.head),
    restOfAnomalies = cs.tail.map(ConfigReadingAnomaly.apply)
  )

object ConfigReadingAnomalies {
  case object ID extends AnomalyID { override val name: String = "config_001" }
}
