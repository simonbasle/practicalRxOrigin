package org.dogepool.practicalrx.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
        int userListSize = allUsers.size();
        CountDownLatch latch = new CountDownLatch(userListSize);
        final List<UserStat> result = Collections.synchronizedList(new ArrayList<>(userListSize));
        for (User user : allUsers) {
            double hashRateForUser = hashrateService.hashrateFor(user);
            coinService.totalCoinsMinedBy(user, new CoinServiceCallback<Long>() {
                @Override
                public void onSuccess(Long totalCoinsMinedByUser) {
                    UserStat userStat = new UserStat(user, hashRateForUser, totalCoinsMinedByUser);
                    result.add(userStat);
                    latch.countDown();
                }
            });

        }
        try {
            latch.await(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
