package org.dogepool.practicalrx.services;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.client.UrlConnectionClient;

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
    private ObjectMapper mapper;

    private Client restClient = new UrlConnectionClient();

    public Double dogeToCurrencyExchangeRate(String targetCurrencyCode) {
        //get the doge-dollar rate
        double doge2usd = dogeToDollar();

        //get the dollar-currency rate
        double usd2currency = dollarToCurrency(targetCurrencyCode);

        //compute the result
        return doge2usd * usd2currency;
    }

    private double dogeToDollar() {
        //TODO timeouts?
        try {
            Response response = restClient.execute(new Request("GET", dogeUrl + "/", null, null));
            if (response.getStatus() == 200) {
                Double rate = mapper.readValue(response.getBody().in(), Double.class);
                return rate.doubleValue();
            } else {
                throw new RuntimeException("Error getting doge to dollar: "
                        + response.getStatus() + " - " + response.getReason());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get doge to dollar", e);
        }
    }

    private double dollarToCurrency(String currencyCode) {
        //TODO timeouts, reading error body
        try {
            Response response = restClient.execute(new Request("GET", exchangeUrl + "/USD/" + currencyCode
                    , null, null));
            if (response.getStatus() == 200) {
                Map<String, Object> result = mapper.readValue(response.getBody().in(), Map.class);
                Double rate = (Double) result.get("exchangeRate");
                if (rate == null)
                    rate = (Double) result.get("rate");
                if (rate == null) {
                    throw new IllegalArgumentException("Malformed exchange rate");
                }
                return rate.doubleValue();
            } else {
                throw new IllegalArgumentException("Error getting USD to " + currencyCode + ": "
                        + response.getStatus() + " - " + response.getReason());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get USD to " + currencyCode + ": " + e.getMessage(), e);
        }
    }
}
