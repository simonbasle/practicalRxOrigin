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

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        SearchService searchService = new SearchService();
        StatService statService = new StatService();
        PoolService poolService = new PoolService();
        AdminService adminService = new AdminService();
        RankingService rankingService = new RankingService();
        HashrateService hashrateService = new HashrateService();
        CoinService coinService = new CoinService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();

        List<User> hashLadder = statService.getLadderByHashrate();
        List<User> coinsLadder = statService.getLadderByCoins();

        System.out.println("Welcome to " + poolService.poolName() + " dogecoin mining pool!");
        System.out.println(poolService.miningUsers().size() + " users currently mining, for a global hashate of "
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
