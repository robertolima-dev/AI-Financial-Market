package com.mali.exchanger.communication.impl.okex.caches;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mali.entity.Market;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexApiException;
import com.mali.exchanger.communication.impl.okex.implementations.OkexApis;
import com.mali.exchanger.communication.impl.okex.sub.entity.MarketString;
import com.mali.exchanger.communication.impl.okex.sub.entity.Markets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class OkexMarketsCache {

    @Autowired
    private OkexApis apis;

    private LoadingCache<String, List<Market>> marketsCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, List<Market>>() {
                        public List<Market> load(String lul) throws OkexApiException {
                            final List<Market> markets = okexMarkets();
                            return markets;
                        }
                    }
            );

    public List<Market> getMarkets() throws ExecutionException,OkexApiException {
        final List<Market> markets = marketsCache.get("OMEGALUL");
        return markets;
    }

    public List<Market> okexMarkets() throws OkexApiException {
        List<Market> markets = new ArrayList<Market>();
        Markets marketsOkex =  apis.getMarkets();
        for (MarketString marketString : marketsOkex.getData()){
            String oldMarket = marketString.getSymbol();
            Market market = new Market();
            market.setBaseCoin(oldMarket.substring(oldMarket.indexOf("_")+1).toUpperCase());
            market.setToCoin(oldMarket.substring(0,oldMarket.indexOf("_")).toUpperCase());
            market.setBaseCoin( market.getBaseCoin().equals("USDT") ? "USD" : market.getBaseCoin() );
            markets.add(market);
        }
        return markets;
    }


}
