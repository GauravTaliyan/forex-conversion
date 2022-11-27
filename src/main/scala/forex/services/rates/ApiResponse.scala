package forex.services.rates

import forex.domain.{Currency, Price, Rate, Timestamp}
import forex.services.rates.errors.Error

import java.time.OffsetDateTime

case class ApiResponse (from: String,
                        to: String,
                        bid: BigDecimal,
                        ask: BigDecimal,
                        price: BigDecimal,
                        time_stamp: OffsetDateTime) {

  def toRate: Either[Error, Rate] =
    (Currency.fromString(from), Currency.fromString(to)) match {
      case (Right(f), Right(t)) =>
        Right(
          Rate(Rate.Pair(f, t), Price(price), Timestamp(time_stamp))
        )
      case _ => Left(Error.OneFrameLookupFailed(f"Currency pair `${from} <-> ${to}` not supported."))
    }
}
