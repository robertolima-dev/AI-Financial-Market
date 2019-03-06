package com.crypfy.elastic.trader.integrations.mali.coin.data;

import com.crypfy.elastic.trader.integrations.mali.coin.data.exception.CoinException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinJson;
import java.util.List;

public interface CoinService {
    public List<CoinJson> findByMarketCapDesc(Integer requiredCoins) throws CoinException;
}
