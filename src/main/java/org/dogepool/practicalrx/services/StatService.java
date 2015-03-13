package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to get stats on the pool, like top 10 ladders for various criteria.
 */
@Service
public class StatService {

    @Autowired
    private HashrateService hashrateService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    public List<User> getLadderByHashrate() {
        List<User> ranking = rankByHashrate();
        if (ranking.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(ranking.subList(0, Math.min(ranking.size(), 10)));
    }

    public List<User> getLadderByCoins() {
        List<User> ranking = rankByCoins();
        if (ranking.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(ranking.subList(0, Math.min(ranking.size(), 10)));
    }

    public List<User> rankByHashrate() {
        List<User> allUsers = userService.findAll();
        List<User> result = new ArrayList<User>(allUsers);
        Collections.sort(result, (o1, o2) -> {
            double h1 = hashrateService.hashrateFor(o1);
            double h2 = hashrateService.hashrateFor(o2);
            double diff = h2 - h1;
            if (diff == 0d) {
                return 0;
            } else {
                return diff > 0d ? 1 : -1;
            }
        });
        return result;
    }

    public List<User> rankByCoins() {
        List<User> allUsers = userService.findAll();
        List<User> result = new ArrayList<>(allUsers);
        Collections.sort(result, (o1, o2) -> {
            long c1 = coinService.totalCoinsMinedBy(o1);
            long c2 = coinService.totalCoinsMinedBy(o2);
            long diff = c2 - c1;
            if (diff == 0L) {
                return 0;
            } else {
                return diff > 0L ? 1 : -1;
            }
        });
        return result;
    }
}
