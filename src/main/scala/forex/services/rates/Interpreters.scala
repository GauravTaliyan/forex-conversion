package forex.services.rates

import cats.effect.Async
import forex.config.OneFrameConfig
import forex.services.CacheService
import forex.services.rates.interpreters._
import sttp.client3.SttpBackend

object Interpreters {
  def live[F[_]: Async](backend: SttpBackend[F, _],
                        cache: CacheService[F],
                        oneFrameConfig: OneFrameConfig): OneFrame[F] = OneFrameLive[F](backend, cache, oneFrameConfig)
}
