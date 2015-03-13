package org.dogepool.practicalrx.services;

import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;

/**
 * Service to retrieve information on the current status of the mining pool
 */
public class PoolService {

    public String poolName() {
        return "Wow Such Pool!";
    }

    public List<User> miningUsers() {
        return Collections.singletonList(User.USER);
    }

    public double poolGigaHashrate() {
        double hashrate = 0d;
        for (User u : miningUsers()) {
            //TODO inject HashrateService and get user's "real" hashrate
            hashrate += u.displayName.length();
        }
        return hashrate;
    }

}
