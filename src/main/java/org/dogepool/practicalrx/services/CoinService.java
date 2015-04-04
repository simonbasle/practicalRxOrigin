package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for getting info on coins mined by users.
 */
@Service
public class CoinService {

    @Autowired
    UserService userService;

    public void totalCoinsMinedBy(User user, CoinServiceCallback<Long> callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Long coins;
                User otherUser = userService.getUser(1);
                if (user == otherUser) {
                    coins = 12L;
                }
                coins = 0L;
                callback.onSuccess(coins);
            }
        });
        t.start();
    }
}
