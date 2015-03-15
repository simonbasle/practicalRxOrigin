package org.dogepool.practicalrx;

import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.services.AdminService;
import org.dogepool.practicalrx.services.CoinService;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.dogepool.practicalrx.services.HashrateService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.services.SearchService;
import org.dogepool.practicalrx.services.StatService;
import org.dogepool.practicalrx.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);

        UserService userService = ctx.getBean(UserService.class);
        SearchService searchService = ctx.getBean(SearchService.class);
        AdminService adminService = ctx.getBean(AdminService.class);
        StatService statService = ctx.getBean(StatService.class);
        PoolService poolService = ctx.getBean(PoolService.class);
        RankingService rankingService = ctx.getBean(RankingService.class);
        HashrateService hashrateService = ctx.getBean(HashrateService.class);
        CoinService coinService = ctx.getBean(CoinService.class);
        ExchangeRateService exchangeRateService = ctx.getBean(ExchangeRateService.class);

        //connect USER automatically
        poolService.connectUser(User.USER);

        //display welcome screen in console
        List<User> hashLadder = statService.getLadderByHashrate();
        List<User> coinsLadder = statService.getLadderByCoins();

        System.out.println("Welcome to " + poolService.poolName() + " dogecoin mining pool!");
        System.out.println(poolService.miningUsers().size() + " users currently mining, for a global hashrate of "
            + poolService.poolGigaHashrate() + " GHash/s");

        System.out.println("1 DOGE = " + exchangeRateService.dogeToCurrencyExchangeRate("USD") + "$");
        System.out.println("1 DOGE = " + exchangeRateService.dogeToCurrencyExchangeRate("EUR") + "€");

        System.out.println("\n----- TOP 10 Miners by Hashrate -----");
        for (User user : hashLadder) {
            System.out.println(rankingService.rankByHashrate(user) + ": "
                    + user.nickname + ", " + hashrateService.hashrateFor(user) + " GHash/s");
        }

        System.out.println("\n----- TOP 10 Miners by Coins Found -----");
        for (User user : coinsLadder) {
            System.out.println(rankingService.rankByCoins(user) + ": "
                    + user.nickname + ", " + coinService.totalCoinsMinedBy(user) + " dogecoins");
        }
    }
}
