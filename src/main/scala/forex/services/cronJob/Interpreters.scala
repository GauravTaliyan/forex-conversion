package forex.services.cronJob

import cats.effect.{Concurrent, Timer}
import forex.config.OneFrameConfig
import forex.services.cronJob.interpreters.CronJobLive
import forex.services.{CacheService, RatesService}

object Interpreters {
  def live[F[_] : Concurrent : Timer](
      config: OneFrameConfig,
      ratesService: RatesService[F],
      cacheService: CacheService[F]): Algebra[F] = CronJobLive(config, ratesService, cacheService)}
