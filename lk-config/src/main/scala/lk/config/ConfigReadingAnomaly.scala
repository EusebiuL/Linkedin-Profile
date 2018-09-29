package lk.config
import busymachines.core.{Anomaly, AnomalyID, InvalidInputFailure}
import pureconfig.error.ConfigReaderFailure

final case class ConfigReadingAnomaly(c: ConfigReaderFailure)
    extends InvalidInputFailure(
      s"Failed to read config because: ${c.description}"){

  override def parameters: Anomaly.Parameters = {
    val orig: Anomaly.Parameters = Anomaly.Parameters(
      "reason" -> c.description
    )
    val loc = c.location.map(l => ("location" -> l.description): (String, Anomaly.Parameter))
    orig.++(loc.toMap: Anomaly.Parameters)
  }
}

