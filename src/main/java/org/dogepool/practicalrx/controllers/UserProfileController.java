package org.dogepool.practicalrx.controllers;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserProfile;
import org.dogepool.practicalrx.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(value = "/miner", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProfileController {
    
    @Value(value = "${avatar.api.baseUrl}")
    private String avatarBaseUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private HashrateService hashrateService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("{id}")
    public DeferredResult<ResponseEntity<UserProfile>> profile(@PathVariable int id) {
        DeferredResult<ResponseEntity<UserProfile>> deffered = new DeferredResult<>(90000);
        User user = userService.getUser(id);
        if (user == null) {
            deffered.setErrorResult(ResponseEntity.notFound());
            return deffered;
        } else {
            //find the avatar's url
            ResponseEntity<Map> avatarResponse = restTemplate.getForEntity(avatarBaseUrl + "/" + user.avatarId, Map.class);
            if (avatarResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, ?> avatarInfo = avatarResponse.getBody();
                String avatarUrl = (String) avatarInfo.get("large");
                String smallAvatarUrl = (String) avatarInfo.get("small");

                //complete with other information
                double hash = hashrateService.hashrateFor(user);
                long rankByHash = rankingService.rankByHashrate(user);
                long rankByCoins = rankingService.rankByCoins(user);
                coinService.totalCoinsMinedBy(user, new CoinServiceCallback<Long>() {
                    @Override
                    public void onSuccess(Long coins) {
                        ResponseEntity<UserProfile> response = ResponseEntity.ok(new UserProfile(user, hash, coins, avatarUrl, smallAvatarUrl, rankByHash, rankByCoins));
                        deffered.setResult(response);
                    }
                });

                return deffered;
            } else {
                deffered.setErrorResult(new ResponseEntity<UserProfile>(avatarResponse.getStatusCode()));
                return deffered;
            }
        }
    }

    @RequestMapping(value = "{id}", produces = MediaType.TEXT_HTML_VALUE)
    public DeferredResult<ResponseEntity<String>> profileHtml(@PathVariable int id) {
        DeferredResult<ResponseEntity<UserProfile>> defferedResponseProfile = profile(id);
        defferedResponseProfile.getResult();
        DeferredResult<ResponseEntity<String>> stringResponse = new DeferredResult<ResponseEntity<String>>();
        defferedResponseProfile.onCompletion(new Runnable() {
            @Override
            public void run() {
                ResponseEntity<UserProfile> responseProfile = (ResponseEntity<UserProfile>) defferedResponseProfile.getResult();
                if (responseProfile.getStatusCode().is2xxSuccessful()) {
                    UserProfile profile = responseProfile.getBody();
                    User user = profile.user;
                    //A template would probably be better but this is poor man's html version
                    StringBuilder sb = new StringBuilder("<html><body>");
                    sb.append("\n<img src='").append(profile.smallAvatarUrl).append("' width=50 height=50/>");
                    sb.append("<h1>").append(user.displayName).append("</h1> - <i>").append(user.nickname);
                    sb.append("\n<p>").append(user.bio).append("</p>");
                    sb.append("\n<br/><img src='").append(profile.avatarUrl).append("'/>");
                    sb.append("\n<br/><h3>Rank by Coins: ").append(profile.rankByCoins).append("</h3>");
                    sb.append("\n<br/><h3>Rank by Hashrate: ").append(profile.rankByHash).append("</h3>");
                    sb.append("\n</body></html>");
                    stringResponse.setResult(ResponseEntity.ok(sb.toString()));
                } else {
                    stringResponse.setErrorResult(new ResponseEntity<String>(responseProfile.getBody().toString(), responseProfile.getHeaders(),
                            responseProfile.getStatusCode()));
                }
            }
        });
        return stringResponse;
    }
}
