package com.mali.coin.api;

import com.mali.coin.coinmarketcap.exceptions.CoinMarketCapAPIException;
import com.mali.coin.exceptions.CoinException;
import com.mali.persistence.entity.Coin;
import com.mali.persistence.entity.CoinHistory;

import java.math.BigDecimal;
import java.util.List;

public interface CoinService {
    public void addOrUpdateCoins() throws CoinMarketCapAPIException;

    public Coin add(Coin coin) throws CoinException;
    public List<Coin> findAllByOrderByMarketCapUsdDesc(int requiredElements);
    public List<Coin> findByMarketCapUsdGreaterThan(BigDecimal marketCap);
    public List<String> findByHistory(int requiredElements);
}
