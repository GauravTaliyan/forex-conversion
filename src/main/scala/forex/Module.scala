package forex

import cats.effect.{Concurrent, Timer}
import forex.config.ApplicationConfig
import forex.http.rates.RatesHttpRoutes
import forex.services._
import forex.programs._
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.middleware.{AutoSlash, Timeout}
import sttp.client3.SttpBackend
import cats.effect.concurrent.Ref
import com.typesafe.scalalogging.Logger
import forex.domain.Rate

class Module[F[_]: Concurrent: Timer](config: ApplicationConfig,
                                      cache: Ref[F, Map[Rate.Pair, Rate]],
                                      sttpBackend: SttpBackend[F, _]) {

  val logger = Logger(getClass.getName)

  private val cacheService: CacheService[F] = CacheServices.live[F](cache)

  private val ratesService: RatesService[F] = RatesServices.live[F](sttpBackend, cacheService, config.oneFrameConfig)

  private val ratesProgram: RatesProgram[F] = RatesProgram[F](ratesService)

  private val ratesHttpRoutes: HttpRoutes[F] = new RatesHttpRoutes[F](ratesProgram).routes

  val cronJob: CronJobService[F] =
    CronJobServices.live[F](config.oneFrameConfig, ratesService, cacheService)

  type PartialMiddleware = HttpRoutes[F] => HttpRoutes[F]
  type TotalMiddleware   = HttpApp[F] => HttpApp[F]

  private val routesMiddleware: PartialMiddleware = {
    { http: HttpRoutes[F] =>
      AutoSlash(http)
    }
  }

  private val appMiddleware: TotalMiddleware = { http: HttpApp[F] =>
    Timeout(config.http.timeout)(http)
  }

  private val http: HttpRoutes[F] = ratesHttpRoutes

  val httpApp: HttpApp[F] = appMiddleware(routesMiddleware(http).orNotFound)

}
