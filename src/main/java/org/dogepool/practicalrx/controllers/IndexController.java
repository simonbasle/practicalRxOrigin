package org.dogepool.practicalrx.controllers;

import org.dogepool.practicalrx.domain.UserStat;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.views.models.IndexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A utility controller that displays the welcome message as HTML on root endpoint.
 */
@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private RankingService rankService;

    @Autowired
    private PoolService poolService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        //prepare a model
        IndexModel idxModel = new IndexModel();
        idxModel.setHashLadder(rankService.getLadderByHashrate());
        idxModel.setCoinsLadder(rankService.getLadderByCoins());
        idxModel.setPoolName(poolService.poolName());
        idxModel.setMiningUserCount(poolService.miningUsers().size());
        idxModel.setGigaHashrate(poolService.poolGigaHashrate());
        try {
            idxModel.setDogeToUsdMessage("1 DOGE = " + exchangeRateService.dogeToCurrencyExchangeRate("USD") + "$");
        } catch (Exception e) {
            idxModel.setDogeToUsdMessage("1 DOGE = ??$, couldn't get the exchange rate - " + e);
        }
        try {
            idxModel.setDogeToEurMessage("1 DOGE = " + exchangeRateService.dogeToCurrencyExchangeRate("EUR") + "€");
        } catch (Exception e) {
            idxModel.setDogeToEurMessage("1 DOGE = ??€, couldn't get the exchange rate - " + e);
        }

        //build the HTML
        StringBuilder html = new StringBuilder("<html><body>");

        html.append("<h1>Welcome to " + idxModel.getPoolName() + " dogecoin mining pool</h1>");
        html.append("<p>" + idxModel.getMiningUserCount() + " users currently mining, for a global hashrate of "
                + idxModel.getGigaHashrate() + " GHash/s</p>");

        html.append("<p>").append(idxModel.getDogeToUsdMessage()).append("<br/>");
        html.append(idxModel.getDogeToEurMessage()).append("</p>");

        html.append("<p><h3>----- TOP 10 Miners by Hashrate -----</h3>");
        int rank = 1;
        for (UserStat userStat : idxModel.getHashLadder()) {
            html.append("<br/>").append(rank++)
                .append(": ").append(userStat.user.nickname)
                .append(", ").append(userStat.hashrate).append(" GHash/s");
        }
        html.append("</p>");

        html.append("<p><h3>----- TOP 10 Miners by Coins Found -----</h3>");
        rank = 1;
        for (UserStat userStat : idxModel.getCoinsLadder()) {
            html.append("<br/>").append(rank++).append(": ")
                .append(userStat.user.nickname).append(", ")
                .append(userStat.totalCoinsMined).append(" dogecoins");
        }
        html.append("</p>");
        html.append("</body></html>");
        return html.toString();
    }
}
