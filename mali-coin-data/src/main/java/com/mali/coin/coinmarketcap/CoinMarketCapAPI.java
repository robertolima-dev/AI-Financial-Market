package com.mali.coin.coinmarketcap;

import com.mali.coin.coinmarketcap.exceptions.CoinMarketCapAPIException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CoinMarketCapAPI {

    public static final String COIN_MARKET_CAP_RESOURCE = "https://api.coinmarketcap.com/";
    public static final String COIN_MARKET_CAP_RESOURCE_TICKER = "v1/ticker/?limit=101";

    public List<CoinMarketCapTicker> readCoins() throws CoinMarketCapAPIException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CoinMarketCapTicker[]> coins = restTemplate.getForEntity(COIN_MARKET_CAP_RESOURCE + COIN_MARKET_CAP_RESOURCE_TICKER, CoinMarketCapTicker[].class);
            return new ArrayList<CoinMarketCapTicker>(Arrays.asList(coins.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoinMarketCapAPIException("error on consumes coin market cap api");
        }
    }

}
