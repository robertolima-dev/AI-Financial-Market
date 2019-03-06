package com.mali.controller;

import com.mali.coin.api.CoinService;
import com.mali.coin.coinmarketcap.CoinMarketCapAPI;
import com.mali.coin.coinmarketcap.CoinMarketCapTicker;
import com.mali.coin.coinmarketcap.exceptions.CoinMarketCapAPIException;
import com.mali.crawler.CoinDataCoinMarketCapSite;
import com.mali.crawler.api.CoinDataExtractor;
import com.mali.crawler.exception.ReadDataCoinException;
import com.mali.persistence.entity.Coin;
import com.mali.persistence.entity.CoinHistory;
import com.mali.persistence.repository.CoinHistoryRepository;
import com.mali.persistence.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class MainController {
    @Autowired
    private CoinHistoryRepository coinHistoryRepository;
    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private CoinMarketCapAPI coinMarketCapAPI;
    @Autowired
    private CoinDataExtractor<CoinDataCoinMarketCapSite> coinDataExtractor;
    @Autowired
    private CoinService coinService;

    @GetMapping(path = "/start-coin-history")
    public String startCoinHistory() {
        List<CoinMarketCapTicker> coins = null;
        try {
            coins = coinMarketCapAPI.readCoins();
        } catch (CoinMarketCapAPIException e) {
            e.printStackTrace();
        }
        for (CoinMarketCapTicker coin : coins) {
            if (!coin.getId().equals("tether")) {
                try {
                    List<CoinDataCoinMarketCapSite> ticks = coinDataExtractor.read(coin.getId());
                    for (CoinDataCoinMarketCapSite tick : ticks) {
                        CoinHistory coinHistory = new CoinHistory();
                        coinHistory.setOpenValue(tick.getOpen());
                        coinHistory.setCloseValue(tick.getClose());
                        coinHistory.setLowValue(tick.getLow());
                        coinHistory.setHighValue(tick.getHigh());
                        coinHistory.setVolume(tick.getVolume());
                        coinHistory.setMarketcap(tick.getMarketcap());
                        coinHistory.setCreated(new Date());
                        coinHistory.setIdcoin(coin.getId());
                        coinHistory.setDate(tick.getDate());
                        coinHistory.setSymbol(coin.getSymbol());

                        //try add coin history
                        coinHistoryRepository.save(coinHistory);
                        System.out.println("inserted history for coin " + coin.getName() + " at date " + coinHistory.getDate().toString());
                    }
                } catch (ReadDataCoinException e) {
                    e.printStackTrace();
                }
            }
        }
        return "done";
    }

    @GetMapping(path = "/start-coin")
    public String startCoin() {
        try {
            coinService.addOrUpdateCoins();
        } catch (CoinMarketCapAPIException e) {
            e.printStackTrace();
        }
        return "done";
    }

}
