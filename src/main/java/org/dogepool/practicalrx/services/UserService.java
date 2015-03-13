package org.dogepool.practicalrx.services;

import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;

/**
 * Service to get user information.
 */
public class UserService {

    public User getUser(long id) {
        return User.USER;
    }

    public User getUserByLogin(String login) {
        return User.USER;
    }

    public List<User> findAll() {
        return Collections.singletonList(User.USER);
    }
}
