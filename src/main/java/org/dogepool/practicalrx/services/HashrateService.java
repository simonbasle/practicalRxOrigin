package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;

/**
 * Service to retrieve hashrate information of users.
 */
public class HashrateService {

    /**
     * @param user
     * @return the last known gigahash/sec hashrate for the given user
     */
    public double hashrateFor(User user) {
        return 0.1345;
    }
}
