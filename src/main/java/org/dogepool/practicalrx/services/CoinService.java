package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.stereotype.Service;

/**
 * Service for getting info on coins mined by users.
 */
@Service
public class CoinService {

    public void totalCoinsMinedBy(User user, ServiceCallback<Long> callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Long coins;
                if (user.equals(User.OTHERUSER)) {
                    coins = 12L;
                } else {
                    coins = 0L;
                }
                callback.onSuccess(coins);
            }
        });
        t.start();
    }
}
