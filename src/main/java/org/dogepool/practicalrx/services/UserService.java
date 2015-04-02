package org.dogepool.practicalrx.services;

import java.util.Arrays;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to get user information.
 */
@Service
public class UserService {
    private static List<User> ALL_USERS = Arrays.asList(User.USER, User.OTHERUSER);

    public Observable<User> getUser(long id) {
        return findAll()
                .filter(u -> u.id == id)
                .last();
    }

    public Observable<User> getUserByLogin(String login) {
        return findAll()
                .filter(u -> login.equals(u.nickname))
                .last();
    }

    public Observable<User> findAll() {
        return Observable.from(ALL_USERS);
    }
}
