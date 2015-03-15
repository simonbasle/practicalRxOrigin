package org.dogepool.practicalrx.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.StatService;
import org.dogepool.practicalrx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pool", produces = MediaType.APPLICATION_JSON_VALUE)
public class PoolController {

    @Autowired
    private UserService userService;

    @Autowired
    private StatService statService;

    @Autowired
    private PoolService poolService;

    @RequestMapping("/ladder/hashrate")
    public List<User> ladderByHashrate() {
        return statService.getLadderByHashrate();
    }

    @RequestMapping("/ladder/coins")
    public List<User> ladderByCoins() {
        return statService.getLadderByCoins();
    }

    @RequestMapping("/hashrate")
    public Map<String, Object> globalHashRate() {
        Map<String, Object> json = new HashMap<>(2);
        double ghashrate = poolService.poolGigaHashrate();
        if (ghashrate < 1) {
            json.put("unit", "MHash/s");
            json.put("hashrate", ghashrate * 100d);
        } else {
            json.put("unit", "GHash/s");
            json.put("hashrate", poolService.poolGigaHashrate());
        }
        return json;
    }

    @RequestMapping("/miners")
    public Map<String, Object> miners() {
        Map<String, Object> json = new HashMap<>(2);
        json.put("totalUsers", userService.findAll().size());
        json.put("totalMiningUsers", poolService.miningUsers().size());
        return json;
    }

    @RequestMapping("/miners/active")
    public List<User> activeMiners() {
        return poolService.miningUsers();
    }
}
