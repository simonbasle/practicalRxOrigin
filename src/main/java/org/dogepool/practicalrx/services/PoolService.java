package org.dogepool.practicalrx.services;

import java.util.HashSet;
import java.util.Set;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

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

    private final Set<User> connectedUsers = new HashSet<>();

    public Observable<User> miningUsers() {
        return Observable.from(connectedUsers);
    }

    public Observable<Double> poolGigaHashrate() {
        return miningUsers()
                .flatMap(u -> hashrateService.hashrateFor(u))
                .reduce(0d, (pools, users) -> pools + users);
    }

    public Observable<Boolean> connectUser(User user) {
        return Observable
                .<Boolean>create(s -> {
                    connectedUsers.add(user);
                    s.onNext(Boolean.TRUE);
                    s.onCompleted();
                })
                .doOnNext(v -> System.out.println("Connected users (+1): " + connectedUsers));
    }

    public Observable<Boolean> disconnectUser(User user) {
        return Observable
                .<Boolean>create(s -> {
                    connectedUsers.remove(user);
                    s.onNext(Boolean.TRUE);
                    s.onCompleted();
                })
                .doOnNext(v -> System.out.println("Connected users (-1): " + connectedUsers));
    }
}
