package com.mali.exchanger.communication.impl.binance.caches;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BinanceFactoryCache {

    private  List<String> list = new ArrayList<>();

    private LoadingCache<List<String>, BinanceApiClientFactory> factoryCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<List<String>, BinanceApiClientFactory>() {
                        public BinanceApiClientFactory load(List<String> list) {
                            BinanceApiClientFactory  factory = binanceFactory(list.get(0), list.get(1));
                            return factory;
                        }
                    }
            );

    public BinanceApiClientFactory getFactory(String key,String secret) throws ExecutionException {
        list.add(key);
        list.add(secret);
        BinanceApiClientFactory  factory = factoryCache.get(list);
        return factory;
    }

    private BinanceApiClientFactory binanceFactory(String key,String secret){
        return BinanceApiClientFactory.newInstance(key,secret);
    }

}
