package org.dogepool.practicalrx;

import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.RankingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);

        RankingService rankinService = ctx.getBean(RankingService.class);
        PoolService poolService = ctx.getBean(PoolService.class);
        ExchangeRateService exchangeRateService = ctx.getBean(ExchangeRateService.class);

        //connect USER automatically
        poolService.connectUser(User.USER);

        //display welcome screen in console
        List<UserStat> hashLadder = rankinService.getLadderByHashrate();
        List<UserStat> coinsLadder = rankinService.getLadderByCoins();

        System.out.println("Welcome to " + poolService.poolName() + " dogecoin mining pool!");
        System.out.println(poolService.miningUsers().size() + " users currently mining, for a global hashrate of "
            + poolService.poolGigaHashrate() + " GHash/s");

        System.out.println("1 DOGE = " + exchangeRateService.dogeToCurrencyExchangeRate("USD") + "$");
        System.out.println("1 DOGE = " + exchangeRateService.dogeToCurrencyExchangeRate("EUR") + "€");

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
    }
}
