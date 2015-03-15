package org.dogepool.practicalrx.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.dogepool.practicalrx.domain.User;
import org.springframework.stereotype.Service;

/**
 * Service to get user information.
 */
@Service
public class UserService {
    private static List<User> ALL_USERS = Arrays.asList(User.USER, User.OTHERUSER);

    public User getUser(long id) {
        for (User user : findAll()) {
            if (user.id == id) {
                return user;
            }
        }

        return null; //TODO any better way of doing this in Java 8?
    }

    public User getUserByLogin(String login) {
        for (User user : findAll()) {
            if (login.equals(user.nickname)) {
                return user;
            }
        }

        return null; //TODO any better way of doing this in Java 8?
    }

    public List<User> findAll() {
        return ALL_USERS;
    }
}
