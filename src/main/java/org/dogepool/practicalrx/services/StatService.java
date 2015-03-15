package org.dogepool.practicalrx.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public List<UserStat> getAllStats() {
        List<User> allUsers = userService.findAll();
        List<UserStat> result = new ArrayList<>(allUsers.size());
        for (User user : allUsers) {
            result.add(new UserStat(user, hashrateService.hashrateFor(user), coinService.totalCoinsMinedBy(user)));
        }
        return result;
    }

    public LocalDateTime lastBlockFoundDate() {
        Random rng = new Random(System.currentTimeMillis());
        return LocalDateTime.now().minus(rng.nextInt(72), ChronoUnit.HOURS);
    }

    public User lastBlockFoundBy() {
        Random rng = new Random(System.currentTimeMillis());
        List<User> allUsers = userService.findAll();
        return allUsers.get(rng.nextInt(allUsers.size()));
    }
}
