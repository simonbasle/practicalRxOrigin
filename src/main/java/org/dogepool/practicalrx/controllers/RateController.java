package org.dogepool.practicalrx.controllers;

import org.dogepool.practicalrx.domain.ExchangeRate;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rate", produces = MediaType.APPLICATION_JSON_VALUE)
public class RateController {

    @Autowired
    private ExchangeRateService service;

    @RequestMapping("{moneyTo}")
    public ResponseEntity rate(@PathVariable String moneyTo) {
        try {
            Double exchange = service.dogeToCurrencyExchangeRate(moneyTo);
            if (exchange == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(new ExchangeRate("DOGE", moneyTo, exchange));
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
