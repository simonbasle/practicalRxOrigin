package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.stereotype.Service;

/**
 * Service for getting info on coins mined by users.
 */
@Service
public class CoinService {

    public long totalCoinsMinedBy(User user) {
        if (user == User.OTHERUSER) {
            return 12L;
        }
        return 0L;
    }
}
