package forex.services.cache.interpreters

import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._
import forex.domain.Rate
import forex.services.cache.Cache
import org.log4s.getLogger

class CacheLive[F[_]: Concurrent](cache: Ref[F, Map[Rate.Pair, Rate]]) extends Cache[F] {
  val logger = getLogger

  def get(pair: Rate.Pair): F[Option[Rate]] =
    cache.get.flatMap(cacheObject => {
      if (cacheObject.isEmpty) logger.error("Cannot Find currency exchange in Cache")
      cacheObject.get(pair).pure[F]
    })

  def set(rates: Map[Rate.Pair, Rate]): F[Unit] =
    cache.set(rates)
}

object CacheLive {
  def apply[F[_]: Concurrent](cache: Ref[F, Map[Rate.Pair, Rate]]): CacheLive[F] = new CacheLive(cache)
}
