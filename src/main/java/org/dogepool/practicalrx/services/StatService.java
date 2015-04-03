package org.dogepool.practicalrx.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.functions.Func1;

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

    public Observable<UserStat> getAllStats() {
        return userService.findAll()
                .flatMap(u -> {
                    Observable<Double> hr = hashrateService.hashrateFor(u);
                    Observable<Long> co = coinService.totalCoinsMinedBy(u);

                    return Observable.zip(hr, co, (rate, coin) -> new UserStat(u, rate, coin));
        });
    }

    public Observable<LocalDateTime> lastBlockFoundDate() {
        Random rng = new Random(System.currentTimeMillis());
        LocalDateTime date = LocalDateTime.now().minus(rng.nextInt(72), ChronoUnit.HOURS);
        return Observable.just(date);
    }

    public Observable<User> lastBlockFoundBy() {
        final Random rng = new Random(System.currentTimeMillis());
        return Observable.<Integer>create(s -> {
            s.onNext(rng.nextInt(10));
            s.onCompleted();
        })
                         .doOnNext(i -> System.out.println(i))
                         .flatMap(i -> userService.findAll().skip(i))
                         .last()
                         .retry(4)
                         .onErrorReturn(t -> new User(-1L, "Default", "Banned User", "default", "0"));
    }
}
