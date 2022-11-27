package forex.services.rates

object Error {

  sealed trait Error
  object Error {
    final case class OneFrameLookupFailed(msg: String) extends Error
  }

}
