package forex.services.rates

import forex.domain.Rate
import Error._

trait OneFrame[F[_]] {
  def get(pair: Rate.Pair): F[Error Either Rate]
  def getAllRates(rates: List[Rate.Pair]): F[Error Either Map[Rate.Pair, Rate]]
}
