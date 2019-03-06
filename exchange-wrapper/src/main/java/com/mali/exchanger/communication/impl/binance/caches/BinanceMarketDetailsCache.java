package com.mali.exchanger.communication.impl.binance.caches;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BinanceMarketDetailsCache {

    private LoadingCache<String, ExchangeInfo> marketsInfo = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30, TimeUnit.DAYS)
            .build(
                    new CacheLoader<String, ExchangeInfo>() {
                        public ExchangeInfo load(String market) {
                            final ExchangeInfo  info = binanceInfo();
                            return info;
                        }
                    }
            );

    public ExchangeInfo  getExchangerInfo() throws ExecutionException {
        final ExchangeInfo  info = marketsInfo.get("");
        return info;
    }

    private ExchangeInfo  binanceInfo(){
        return BinanceApiClientFactory.newInstance("","").newRestClient().getExchangeInfo();
    }

}



