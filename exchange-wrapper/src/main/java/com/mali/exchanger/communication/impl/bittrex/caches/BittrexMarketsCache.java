package com.mali.exchanger.communication.impl.bittrex.caches;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mali.entity.Market;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexApiException;
import com.mali.exchanger.communication.impl.bittrex.implementations.BittrexApis;
import com.mali.exchanger.communication.impl.bittrex.sub.entity.CoinDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BittrexMarketsCache {

    @Autowired
    private BittrexApis apis;

    private LoadingCache<String, List<Market>> marketsCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, List<Market>>() {
                        public List<Market> load(String lul) throws BittrexApiException {
                            final List<Market> markets = bittrexMarkets();
                            return markets;
                        }
                    }
            );

    public List<Market> getMarkets() throws ExecutionException,BittrexApiException {
        final List<Market> markets = marketsCache.get("OMEGALUL");
        return markets;
    }

    public List<Market> bittrexMarkets() throws BittrexApiException {
        List<Market> markets = new ArrayList<Market>();
        for(CoinDetail coinDetail : apis.getMarketList().getResult()){
            Market market = new Market();
            market.setToCoin( coinDetail.getMarketCurrency().equals("USDT") ? "USD" : coinDetail.getMarketCurrency());
            market.setBaseCoin( coinDetail.getBaseCurrency().equals("USDT") ? "USD" : coinDetail.getBaseCurrency());

            markets.add(market);
        }
        return markets;
    }

}
