package forex.services.rates.interpreters

import cats.effect.Async
import cats.implicits._
import forex.config.OneFrameConfig
import forex.domain.Rate
import forex.services._
import forex.services.rates.errors.Error
import forex.services.rates.{Algebra, ApiResponse}
import io.circe.generic.auto._
import sttp.client3.{SttpBackend, UriContext, basicRequest}
import sttp.client3.circe.asJson
import org.log4s._

class OneFrameLive[F[_]: Async](backend: SttpBackend[F, _], cache: CacheService[F], oneFrameConfig: OneFrameConfig)
  extends Algebra[F] {
  val logger = getLogger

  override def get(pair: Rate.Pair): F[Error Either Rate] =

    for {
      rateOpt <- cache.get(pair)
      rate = rateOpt.toRight(Error.OneFrameLookupFailed("Rate not found in cache"))
    } yield rate

  override def getAllRates(rates: List[Rate.Pair]): F[Error Either Map[Rate.Pair, Rate]] = for {
    response <- backend.send(requestObject(rates))
    body = response.body.leftMap(e => Error.OneFrameLookupFailed(e.toString()))
    allRates = body.flatMap { rate =>
      val (lefts, right) = rate.map(_.toRate).partitionMap(identity)
      lefts.headOption
        .toLeft(right.map(r => (r.pair, r)).toMap)
    }
  } yield allRates

  private def requestObject(pairs: List[Rate.Pair]) = {

    val paramsTuple = pairs.map(p => (OneFrameLive.pair, p.toString()))
    val uri = uri"${oneFrameConfig.host}/${OneFrameLive.path}"
      .addParams(paramsTuple: _*)

    basicRequest
      .header(OneFrameLive.token, oneFrameConfig.token)
      .get(uri)
      .response(asJson[List[ApiResponse]])
  }
}

object OneFrameLive {

  val token = "Token"
  val pair = "pair"
  var path = "rates"
  def apply[F[_]: Async](backend: SttpBackend[F, _], cache: CacheService[F], oneFrameConfig: OneFrameConfig): Algebra[F] =
    new OneFrameLive[F](backend, cache, oneFrameConfig)
}
