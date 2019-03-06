package com.mali.exchanger.communication.impl.bitfinex.implementations.caches;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mali.entity.Market;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexApiException;
import com.mali.exchanger.communication.impl.bitfinex.implementations.BitfinexApis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BitfinexMarketsCache {

    @Autowired
    private BitfinexApis apis;

    private LoadingCache<String, List<Market>> marketsCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, List<Market>>() {
                        public List<Market> load(String lul) throws BitfinexApiException {
                            final List<Market> markets = bitfinexMarkets();
                            return markets;
                        }
                    }
            );

    public List<Market> getMarkets() throws ExecutionException,BitfinexApiException {
        final List<Market> markets = marketsCache.get("OMEGALUL");
        return markets;
    }

    public List<Market> bitfinexMarkets() throws BitfinexApiException {
        List<Market> markets = new ArrayList<Market>();
        for(String marketName : apis.getMarketList()){
            int lenght = marketName.length();
            Market market = new Market();
            market.setBaseCoin(marketName.substring(lenght-3,lenght).toUpperCase());
            market.setToCoin(marketName.substring(0,lenght-3).toUpperCase());

            markets.add(market);
        }
        return markets;
    }

}
