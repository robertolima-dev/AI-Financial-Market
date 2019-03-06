package com.mali.exchanger.communication.impl.binance.caches;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.general.SymbolInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mali.entity.Market;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import com.mali.utils.ExchangerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BinanceMarketsCache {

    @Autowired
    ExchangerUtils exchangerUtils;


    private LoadingCache<String, List<Market>> marketsCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, List<Market>>() {
                        public List<Market> load(String lul) throws ExchangerServicesException {
                            final List<Market> markets = binanceMarkets();
                            return markets;
                        }
                    }
            );

    public List<Market> getMarkets() throws ExecutionException {
        final List<Market> markets = marketsCache.get("OMEGALUL");
        return markets;
    }

    private List<Market> binanceMarkets() throws ExchangerServicesException {
        List<Market> markets = new ArrayList<>();
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("","");
        BinanceApiRestClient client = factory.newRestClient();
        List<SymbolInfo> symbols = client.getExchangeInfo().getSymbols();
        for (SymbolInfo symbolInfo : symbols){
            Market market = new Market();
            market.setToCoin(symbolInfo.getBaseAsset());
            market.setBaseCoin(symbolInfo.getQuoteAsset().equals("USDT") ? "USD" : symbolInfo.getQuoteAsset());
            markets.add(market);
        }
        return markets;
    }

}
