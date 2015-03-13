package org.dogepool.practicalrx.services;

import java.math.BigInteger;
import java.time.Month;

import org.springframework.stereotype.Service;

/**
 * Service for administrative purpose like tracking operational costs.
 */
@Service
public class AdminService {

    public BigInteger costForMonth(Month month) {
        return BigInteger.valueOf(month.getValue() * 2000L);
    }
}
