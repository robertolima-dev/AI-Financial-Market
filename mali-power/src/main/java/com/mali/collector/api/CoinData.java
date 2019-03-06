package com.mali.collector.api;

import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.collector.json.CoinHistoryJson;
import com.mali.collector.json.CoinJson;

import java.math.BigDecimal;
import java.util.List;

public interface CoinData {
    public List<CoinHistoryJson> coinHistoryByIdcoin(String idcoin) throws CoinDataAPIException;
    public List<CoinJson> all() throws CoinDataAPIException;
    public List<CoinJson> coinByMarketCap(BigDecimal marketCap) throws CoinDataAPIException;
}
