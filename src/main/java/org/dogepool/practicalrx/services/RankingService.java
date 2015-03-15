package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to get a user's rankings in the pool.
 */
@Service
public class RankingService {

    @Autowired
    private StatService statService;

    public int rankByHashrate(User user) {
        return 1; //FIXME
    }

    public int rankByCoins(User user) {
        return 1; //FIXME
    }
}
