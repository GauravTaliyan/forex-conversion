package forex.domain

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class CurrencyTest extends AnyFunSpec {
  describe("A currency") {
    it("return all pairs" ) {
        Currency.allPairs.size shouldBe 72
    }

    it("should be able to represent itself as a string") {
      val currency = Currency.AUD
      assert(currency.toString === "AUD")
    }

    it("should be able to be parsed from supported strings") {
      Currency.fromString("AUD") match {
        case Right(c) => assert(c.toString === "AUD")
        case Left(_)  => fail()
      }
    }

    it("should be able to be show supported strings") {
      Currency.show.show(Currency.AUD) shouldBe "AUD"
    }

    it("should throw an error on parsing of unsupported strings") {
      Currency.fromString("XOXOXO") match {
        case Right(_) => fail()
        case Left(_)  => succeed
      }
    }
  }

}
