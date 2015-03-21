package org.dogepool.practicalrx.controllers;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserProfile;
import org.dogepool.practicalrx.services.CoinService;
import org.dogepool.practicalrx.services.HashrateService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/miner", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProfileController {
    
    @Value(value = "${avatar.api.avatarLargeUrl}")
    private String avatarApiUrlFormat;

    @Value(value = "${avatar.api.avatarSmallUrl}")
    private String avatarApiSmallUrlFormat;

    @Autowired
    private UserService userService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private HashrateService hashrateService;

    @Autowired
    private CoinService coinService;

    @RequestMapping("{id}")
    public ResponseEntity<UserProfile> profile(@PathVariable int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return (ResponseEntity<UserProfile>) ResponseEntity.notFound();
        } else {
            double hash = hashrateService.hashrateFor(user);
            long coins = coinService.totalCoinsMinedBy(user);
            String avatarUrl = String.format(avatarApiUrlFormat, user.avatarId);
            String smallAvatarUrl = String.format(avatarApiSmallUrlFormat, user.avatarId);
            long rankByHash = rankingService.rankByHashrate(user);
            long rankByCoins = rankingService.rankByCoins(user);

            return ResponseEntity.ok(new UserProfile(user, hash, coins, avatarUrl, smallAvatarUrl, rankByHash, rankByCoins));
        }
    }
}
