# A local proxy for Forex rate conversion

Forex is a simple application that acts as a local proxy for getting exchange rates. It's a service that can be consumed by other internal services to get the exchange rate between a set of currencies, so they don't have to care about the specifics of third-party providers.

## Project Workflow
Proxy has a Cron job which run every 2 mins and fetches the latest exchange rates from the third-party provider. 
The exchange rates are stored in an in memory cache. 
The cache is then used by the API to get the exchange rate between a set of currencies.
If we are not able to fetch currency from the third-party provider, we will invalidate the cache to avoid serving stale data.

## 1000 Request Limitation 
The third-party provider has a limitation of 1000 requests per month.
Proxy will fetch the data from cache for every request, so we can serve the data without hitting the third-party provider.
Cron job will always keep the cache updated. Fetching the data from one-frame-api every 2 mins.

No. of Call per Hour = 60/2 = 30
No. of Call per Month = 30 * 24 = 720

## API
The API is a simple HTTP service that exposes a single endpoint to get the exchange rate between a set of currencies.
GET /rates?pair={currency_pair_0}&pair={currency_pair_1}&...pair={currency_pair_n}
pair: Required query parameter that is the concatenation of two different currency codes, e.g. USDJPY. One or more pairs per request are allowed.

## Limitations
Current Api Support 9 Currencies Only. But these can be extended by adding more currencies in the currency file.

## Example
```curl -X GET "http://localhost:8080/rates?pair=USDJPY&pair=USDEUR"```

## Usage

Run Dependency
```docker run -p 8081:8080 --name one-frame-service -d --rm paidyinc/one-frame:latest```

Compile Application
```sbt compile```

Run Application
```sbt run```

Run Test
```sbt test```

## Logging
- Support for logging is provided by the logback library. The configuration file is located at src/main/resources/logback.xml.
- Appropriate Error message will be return by api in case of any error fetching the rates from api.
- Logs will be added to stdout.
- In case of any error while fetching the rates from one-frame-api via cron job, it will be logged and cache will be invalidated.

## Improvements
- Can use redis cache instead of in memory cache.
- Can fetch api token from vault (Like Hashicorp Vault) instead of hardcoding it in the code.
- Docker Support
- Add more Unit Test for the code.
