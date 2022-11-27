package forex.services.cache

import forex.domain.Rate

trait Cache[F[_]] {

  def get(pair: Rate.Pair): F[Option[Rate]]

  def set(rates: Map[Rate.Pair, Rate]): F[Unit]
}
