package org.dogepool.practicalrx.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

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

    public Observable<Double> dogeToCurrencyExchangeRate(String targetCurrencyCode) {
        return dogeToDollar()
                .zipWith(dollarToCurrency(targetCurrencyCode),
                        (doge2usd, usd2currency) -> doge2usd * usd2currency);
    }

    private Observable<Double> dogeToDollar() {
        return Observable.create(sub -> {
            try {
                Double rate = restTemplate.getForObject(dogeUrl, Double.class);
                sub.onNext(rate);
                sub.onCompleted();
            } catch (Exception e) {
                sub.onError(e);
            }
        });
    }

    private Observable<Double> dollarToCurrency(String currencyCode) {
        return Observable.<Double>create(sub -> {
            Map result = restTemplate.getForObject(exchangeUrl + "/{from}/{to}", Map.class,
                    "USD", currencyCode);
            Double rate = (Double) result.get("exchangeRate");
            if (rate == null)
                rate = (Double) result.get("rate");

            if (rate == null) {
                sub.onError(new IllegalArgumentException("Malformed exchange rate"));
            }
            sub.onNext(rate);
            sub.onCompleted();
        });
    }
}
