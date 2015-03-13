package org.dogepool.practicalrx.services;

import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to retrieve information on the current status of the mining pool
 */
@Service
public class PoolService {

    @Autowired
    private HashrateService hashrateService;

    public String poolName() {
        return "Wow Such Pool!";
    }

    public List<User> miningUsers() {
        return Collections.singletonList(User.USER);
    }

    public double poolGigaHashrate() {
        double hashrate = 0d;
        for (User u : miningUsers()) {
            double userRate = hashrateService.hashrateFor(u);
            hashrate += userRate;
        }
        return hashrate;
    }

}
