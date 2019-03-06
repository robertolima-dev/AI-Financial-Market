package com.mali.api;

import com.mali.api.json.RestResponseJSON;
import com.mali.coin.api.CoinService;
import com.mali.coin.exceptions.CoinException;
import com.mali.persistence.entity.Coin;
import com.mali.persistence.entity.CoinHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class CoinRestful {

    @Autowired
    private CoinService coinService;


    @RequestMapping("/coins")
    public ResponseEntity<RestResponseJSON> findAllByOrderByMarketCapUsdDesc (@RequestParam int requiredCoins) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<Coin> coins = coinService.findAllByOrderByMarketCapUsdDesc(requiredCoins);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(coins);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }

    }

    @RequestMapping("/coins/market-cap-greater-than")
    public List<Coin> listByMarketCapGreatherThan(@RequestParam("marketcap") BigDecimal marketCap) {
        return coinService.findByMarketCapUsdGreaterThan(marketCap);
    }

    @RequestMapping("/coinsString")
    public ResponseEntity<RestResponseJSON> findCoinIds (@RequestParam int requiredCoins) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<String> coins = coinService.findByHistory(requiredCoins);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(coins);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }

    }

}
