package org.dogepool.practicalrx.services;

import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;

/**
 * Service to get stats on the pool, like top 10 ladders for various criteria.
 */
public class StatService {

    public List<User> getLadderByHashrate() {
        return Collections.singletonList(User.USER);
    }

    public List<User> getLadderByCoins() {
        return Collections.singletonList(User.USER);
    }
}
