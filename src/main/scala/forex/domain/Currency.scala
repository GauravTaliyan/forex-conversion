package forex.domain

import cats.Show

sealed trait Currency

object Currency {
  case object AUD extends Currency
  case object CAD extends Currency
  case object CHF extends Currency
  case object EUR extends Currency
  case object GBP extends Currency
  case object NZD extends Currency
  case object JPY extends Currency
  case object SGD extends Currency
  case object USD extends Currency

  implicit val show: Show[Currency] = Show.show {
    case AUD => "AUD"
    case CAD => "CAD"
    case CHF => "CHF"
    case EUR => "EUR"
    case GBP => "GBP"
    case NZD => "NZD"
    case JPY => "JPY"
    case SGD => "SGD"
    case USD => "USD"
  }

  type CurrencyParseError = String
  def fromString(s: String): Either[CurrencyParseError, Currency] = s.toUpperCase match {
    case "AUD" => Right(AUD)
    case "CAD" => Right(CAD)
    case "CHF" => Right(CHF)
    case "EUR" => Right(EUR)
    case "GBP" => Right(GBP)
    case "NZD" => Right(NZD)
    case "JPY" => Right(JPY)
    case "SGD" => Right(SGD)
    case "USD" => Right(USD)
    case unsupportedCurrency => Left(s"Currency: `$unsupportedCurrency` is not supported.")
  }

  val allCurrency = List(AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD)

  def allCurrencyPairs: List[Rate.Pair] = (for {
    currency1 <- allCurrency
    currency2 <- allCurrency
  } yield Rate.Pair(currency1, currency2)).distinct

}
