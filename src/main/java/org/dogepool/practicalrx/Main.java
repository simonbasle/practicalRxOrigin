package org.dogepool.practicalrx;

import java.util.List;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
    }

    @Bean
    @ConditionalOnBean(value = Bucket.class)
    @Order(value = 1)
    CommandLineRunner userCreation(Bucket couchbaseBucket) {
        return args -> {
            JsonDocument u1 = JsonDocument.create(String.valueOf(User.USER.id), User.USER.toJsonObject());
            JsonDocument u2 = JsonDocument.create(String.valueOf(User.OTHERUSER.id), User.OTHERUSER.toJsonObject());
            couchbaseBucket.upsert(u1);
            couchbaseBucket.upsert(u2);
        };
    }

    @Bean
    @Order(value = 2)
    CommandLineRunner commandLineRunner(UserService userService, RankingService rankinService,
            PoolService poolService, ExchangeRateService exchangeRateService) {
        return args -> {
            //connect USER automatically and wait
            boolean connected = userService.getUser(0)
                       .flatMap(user -> poolService.connectUser(user))
                       .toBlocking().singleOrDefault(false);

            //gather data
            List<UserStat> hashLadder = rankinService.getLadderByHashrate().toList().toBlocking().single();
            List<UserStat> coinsLadder = rankinService.getLadderByCoins().toList().toBlocking().single();
            String poolName = poolService.poolName();
            int miningUsersCount = poolService.miningUsers().count().toBlocking().single();
            double poolRate = poolService.poolGigaHashrate().toBlocking().first();
            
            //display welcome screen in console
            System.out.println("Welcome to " + poolName + " dogecoin mining pool!");
            System.out.println(miningUsersCount + " users currently mining, for a global hashrate of "
                    + poolRate + " GHash/s");

            exchangeRateService.dogeToCurrencyExchangeRate("USD").subscribe(
                    r -> System.out.println("1 DOGE = " + r + "$"),
                    e -> System.out.println("1 DOGE = ??$, couldn't get the exchange rate - " + e)
            );
            exchangeRateService.dogeToCurrencyExchangeRate("EUR").subscribe(
                    r -> System.out.println("1 DOGE = " + r + "€"),
                    e -> System.out.println("1 DOGE = ??€, couldn't get the exchange rate - " + e)
            );

            System.out.println("\n----- TOP 10 Miners by Hashrate -----");
            int count = 1;
            for (UserStat userStat : hashLadder) {
                System.out.println(count++ + ": " + userStat.user.nickname + ", " + userStat.hashrate + " GHash/s");
            }

            System.out.println("\n----- TOP 10 Miners by Coins Found -----");
            count = 1;
            for (UserStat userStat : coinsLadder) {
                System.out.println(count++ + ": " + userStat.user.nickname + ", " + userStat.totalCoinsMined + " dogecoins");
            }
        };
    }
}
