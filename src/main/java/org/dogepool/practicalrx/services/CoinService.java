package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service for getting info on coins mined by users.
 */
@Service
public class CoinService {

    @Autowired
    UserService userService;

    public Observable<Long> totalCoinsMinedBy(User user) {
        User otherUser = userService.getUser(1).toBlocking().single();
        if (user == otherUser) {
            return Observable.just(12L);
        }
        return Observable.just(0L);
    }
}
