package forex.programs.rates

import forex.domain.Rate
import Error._

trait Algebra[F[_]] {
  def get(request: Protocol.GetRatesRequest): F[Error Either Rate]
}
