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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(Bucket couchbaseBucket, UserService userService, RankingService rankinService, PoolService poolService, ExchangeRateService exchangeRateService) {
        return args -> {
            JsonDocument u1 = JsonDocument.create(String.valueOf(User.USER.id), User.USER.toJsonObject());
            JsonDocument u2 = JsonDocument.create(String.valueOf(User.OTHERUSER.id), User.OTHERUSER.toJsonObject());
            couchbaseBucket.upsert(u1);
            couchbaseBucket.upsert(u2);

            User user = userService.getUser(0);
            //connect USER automatically
            boolean connected = poolService.connectUser(user);

            //gather data
            List<UserStat> hashLadder = rankinService.getLadderByHashrate();
            List<UserStat> coinsLadder = rankinService.getLadderByCoins();
            String poolName = poolService.poolName();
            List<User> miningUsers = poolService.miningUsers();
            double poolRate = poolService.poolGigaHashrate();

            //display welcome screen in console
            System.out.println("Welcome to " + poolName + " dogecoin mining pool!");
            System.out.println(miningUsers.size() + " users currently mining, for a global hashrate of "
                    + poolRate + " GHash/s");

            try {
                Double dogeToDollar = exchangeRateService.dogeToCurrencyExchangeRate("USD");
                System.out.println("1 DOGE = " + dogeToDollar + "$");
            } catch (Exception e) {
                System.out.println("1 DOGE = ??$, couldn't get the exchange rate - " + e);
            }
            try {
                Double dogeToEuro =  exchangeRateService.dogeToCurrencyExchangeRate("EUR");
                System.out.println("1 DOGE = " + dogeToEuro + "€");
            } catch (Exception e) {
                System.out.println("1 DOGE = ??€, couldn't get the exchange rate - " + e);
            }

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
