package forex.services.cronJob

import fs2.Stream
import scala.concurrent.duration.FiniteDuration
import forex.services.rates.errors.Error

trait Algebra[F[_]] {
  def job(): Stream[F,(Either[Error,Unit], FiniteDuration)]
}
