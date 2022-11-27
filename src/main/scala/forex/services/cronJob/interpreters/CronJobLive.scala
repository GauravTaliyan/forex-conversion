package forex.services.cronJob.interpreters

import cats.data.EitherT
import cats.effect.{Concurrent, Timer}
import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import forex.config.OneFrameConfig
import forex.domain.Currency
import forex.services.cronJob.Algebra
import forex.services.rates.errors.Error
import forex.services.{CacheService, RatesService}
import fs2.Stream

import scala.concurrent.duration.FiniteDuration

class CronJobLive[F[_]: Concurrent: Timer](
      config: OneFrameConfig,
      ratesService: RatesService[F],
      cacheService: CacheService[F])
  extends Algebra[F] {

  override def job(): fs2.Stream[F, (Either[Error, Unit], FiniteDuration)] = Stream(()).repeat
    .evalMap(_ =>
      {
        for {
          allRates <- EitherT(ratesService.getAllRates(Currency.allPairs))
          _ <- EitherT(cacheService.set(allRates).map(_.asRight[Error]))
        } yield ()
      }.value)
    .zip(Stream.awakeEvery[F](config.refreshInterval))
}

object CronJobLive {
  def apply[F[_]: Concurrent: Timer](
      config: OneFrameConfig,
      ratesService: RatesService[F],
      cacheService: CacheService[F]): CronJobLive[F] = new CronJobLive(config, ratesService, cacheService)
}