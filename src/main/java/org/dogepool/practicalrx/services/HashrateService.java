package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to retrieve hashrate information of users.
 */
@Service
public class HashrateService {

    @Autowired
    UserService userService;

    /**
     * @param user
     * @return the last known gigahash/sec hashrate for the given user
     */
    public Observable<Double> hashrateFor(User user) {
        User otherUser = userService.getUser(0).toBlocking().first();
        if (user.equals(otherUser)) {
            return Observable.just(1.234);
        }
        return Observable.just(user.displayName)
                         .map(n -> n.length() / 100d);
    }
}
