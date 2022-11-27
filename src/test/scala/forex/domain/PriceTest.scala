package forex.domain

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class PriceTest extends AnyFunSpec {
  describe("A Price") {
    it("should return price") {
      Price(BigDecimal(0.56)) shouldBe Price(0.56)
    }
  }
}
