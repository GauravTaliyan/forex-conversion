package forex.services.cache

import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import forex.domain.Rate
import forex.services.cache.interpreters.CacheLive

object Interpreters {
  def live[F[_] : Concurrent](cache: Ref[F, Map[Rate.Pair, Rate]]): Cache[F] = CacheLive[F](cache)
}
