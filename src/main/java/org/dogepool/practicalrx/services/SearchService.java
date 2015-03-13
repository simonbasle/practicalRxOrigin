package org.dogepool.practicalrx.services;

import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.springframework.stereotype.Service;

/**
 * A service to search for users by name, login and other criteria.
 */
@Service
public class SearchService {

    public List<User> findByName(String namePattern) {
        //TODO inject UserService and find
        return Collections.singletonList(User.USER);
    }

    public List<User> findByCoins(long minCoins, long maxCoins) {
        //TODO inject UserService, CoinService and find
        return Collections.singletonList(User.USER);
    }
}
