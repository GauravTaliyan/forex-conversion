package forex.domain

import org.scalatest.funspec.AnyFunSpec

import java.time.OffsetDateTime

class RateTest extends AnyFunSpec {
  describe("A Rate") {
    it("should be able to represent its pair as a string") {
      val rate = Rate(Rate.Pair(Currency.GBP, Currency.SGD), Price(BigDecimal(0.56)), Timestamp(OffsetDateTime.now()))

      assert(rate.pair.toString() === "GBPSGD")
    }
  }
}