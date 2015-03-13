package org.dogepool.practicalrx.services;

import org.springframework.stereotype.Service;

/**
 * A facade service to get DOGE to USD and DOGE to other currencies exchange rates.
 */
@Service
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
        //TODO call to external API
        if ("USD".equals(currencyCode)) {
            return 1.0;
        }
        return 2.0;
    }
}
