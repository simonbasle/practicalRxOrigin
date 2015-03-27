package org.dogepool.practicalrx.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * A facade service to get DOGE to USD and DOGE to other currencies exchange rates.
 */
@Service
public class ExchangeRateService {

    @Value("${doge.api.baseUrl}")
    private String dogeUrl;

    @Value("${exchange.free.api.baseUrl}")
    private String exchangeUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Double dogeToCurrencyExchangeRate(String targetCurrencyCode) {
        //get the doge-dollar rate
        double doge2usd = dogeToDollar();

        //get the dollar-currency rate
        double usd2currency = dollarToCurrency(targetCurrencyCode);

        //compute the result
        return doge2usd * usd2currency;
    }

    private double dogeToDollar() {
        return restTemplate.getForObject(dogeUrl, Double.class);
    }

    private double dollarToCurrency(String currencyCode) {
        Map result = restTemplate.getForObject(exchangeUrl + "/{from}/{to}", Map.class,
                "USD", currencyCode);
        Double rate = (Double) result.get("exchangeRate");
        if (rate == null)
            rate = (Double) result.get("rate");

        if (rate == null) {
            throw new IllegalArgumentException("Malformed exchange rate");
        }
        return rate;
    }
}
