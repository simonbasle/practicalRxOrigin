package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
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

    public List<UserStat> getLadderByHashrate() {
        List<UserStat> ranking = rankByHashrate();

        if (ranking.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(ranking.subList(0, Math.min(ranking.size(), 10)));
    }

    public List<UserStat> getLadderByCoins() {
        List<UserStat> ranking = rankByCoins();

        if (ranking.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(ranking.subList(0, Math.min(ranking.size(), 10)));
    }

    protected List<UserStat> getAllStats() {
        List<User> allUsers = userService.findAll();
        List<UserStat> result = new ArrayList<>(allUsers.size());
        for (User user : allUsers) {
            result.add(new UserStat(user, hashrateService.hashrateFor(user), coinService.totalCoinsMinedBy(user)));
        }
        return result;
    }

    protected List<UserStat> rankByHashrate() {
        List<UserStat> result = getAllStats();
        Collections.sort(result, (o1, o2) -> {
            double h1 = o1.hashrate;
            double h2 = o2.hashrate;
            double diff = h2 - h1;
            if (diff == 0d) {
                return 0;
            } else {
                return diff > 0d ? 1 : -1;
            }
        });
        return result;
    }

    protected List<UserStat> rankByCoins() {
        List<UserStat> result = getAllStats();
        Collections.sort(result, (o1, o2) -> {
            long c1 = o1.totalCoinsMined;
            long c2 = o2.totalCoinsMined;
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
