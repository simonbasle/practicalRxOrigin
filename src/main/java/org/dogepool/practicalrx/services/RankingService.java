package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to get ladders and find a user's rankings in the pool.
 */
@Service
public class RankingService {

    @Autowired
    private StatService statService;

    /**
     * Find the user's rank by hashrate in the pool. This is a costly operation.
     * @return the rank of the user in terms of hashrate, or -1 if it couldnt' be established.
     */
    public int rankByHashrate(User user) {
        List<UserStat> rankedByHashrate = rankByHashrate();
        int rank = 1;
        for (UserStat stat : rankedByHashrate) {
            if (stat.user == user) {
                return rank;
            }
            rank++;
        }
        return -1;
    }

    /**
     * Find the user's rank by number of coins found. This is a costly operation.
     * @return the rank of the user in terms of coins found, or -1 if it couldn't be established.
     */
    public int rankByCoins(User user) {
        List<UserStat> rankedByCoins = rankByCoins();
        int rank = 1;
        for (UserStat stat : rankedByCoins) {
            if (stat.user == user) {
                return rank;
            }
            rank++;
        }
        return -1;
    }

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

    protected List<UserStat> rankByHashrate() {
        List<UserStat> result = statService.getAllStats();
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
        List<UserStat> result = statService.getAllStats();
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
