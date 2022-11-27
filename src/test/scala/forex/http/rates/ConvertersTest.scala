package forex.http.rates

import forex.domain.{Currency, Price, Rate, Timestamp}
import org.scalatest.funspec.AnyFunSpec

import java.time.{OffsetDateTime, ZoneOffset}

class ConvertersTest extends AnyFunSpec {
  describe("A Converter") {
    it("should get response from pair") {
      val response = Converters.GetApiResponseOps(Rate(
        Rate.Pair(Currency.AUD, Currency.CAD),
        Price(BigDecimal(0.5D)),
        Timestamp(OffsetDateTime.of(2020, 7, 4, 10, 0, 0, 0, ZoneOffset.UTC))
      ))
      assert(response.rate.pair.toString() === "AUDCAD")
      assert(response.rate.price === Price(BigDecimal(0.5D)))
    }
  }
}
