package org.dogepool.practicalrx.services;

import java.util.ArrayList;
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

    public List<UserStat> getAllStats() {
        List<User> allUsers = userService.findAll();
        List<UserStat> result = new ArrayList<>(allUsers.size());
        for (User user : allUsers) {
            result.add(new UserStat(user, hashrateService.hashrateFor(user), coinService.totalCoinsMinedBy(user)));
        }
        return result;
    }
}
