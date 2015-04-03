package org.dogepool.practicalrx.services;

import com.couchbase.client.java.Bucket;
import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for getting info on coins mined by users.
 */
@Service
public class CoinService {

    @Autowired
    UserService userService;

    public long totalCoinsMinedBy(User user) {
        User otherUser = userService.getUser(1);
        if (user == otherUser) {
            return 12L;
        }
        return 0L;
    }
}
