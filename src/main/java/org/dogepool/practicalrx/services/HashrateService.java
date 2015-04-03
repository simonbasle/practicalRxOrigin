package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public double hashrateFor(User user) {
        User otherUser = userService.getUser(0);
        if (user == otherUser) {
            return 1.234;
        }
        return user.displayName.length() / 100d;
    }
}
