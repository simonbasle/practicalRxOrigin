package org.dogepool.practicalrx.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;

import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service for administrative purpose like tracking operational costs.
 */
@Service
public class AdminService {

    public Observable<BigInteger> costForMonth(int year, Month month) {
        return Observable
                .<LocalDate>create(s -> {
                    s.onNext(LocalDate.now());
                    s.onCompleted();
                })
                .map(now -> {
                    if (year > now.getYear()
                            || year == now.getYear() && month.getValue() > now.getMonthValue()) {
                        return BigInteger.ZERO;
                    }
                    return BigInteger.valueOf(year + month.getValue() * 100);
                });
    }
}
