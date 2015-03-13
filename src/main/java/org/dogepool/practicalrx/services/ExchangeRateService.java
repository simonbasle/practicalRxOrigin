package org.dogepool.practicalrx.services;

/**
 * A facade service to get DOGE to USD and DOGE to other currencies exchange rates.
 */
public class ExchangeRateService {

    public Double dogeToCurrencyExchangeRate(String targetCurrencyCode) {
        //get the doge-dollar rate
        double doge2usd = dogeToDollar();

        //get the dollar-currency rate
        double usd2currency = dollarToCurrency(targetCurrencyCode);

        //compute the result
        return doge2usd * usd2currency;
    }

    private double dogeToDollar() {
        return 0.25;
    }

    private double dollarToCurrency(String currencyCode) {
        if ("USD".equals(currencyCode)) {
            return 1.0;
        }
        return 2.0;
    }
}
