package com.mali.crypfy.clustering.integrations.mali.coin.data;

import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinJson;
import java.util.List;

public interface CoinService {

    public List<CoinJson> coinsByMarketCapDesc(Integer requiredCoins) throws CoinException;
    public List<CoinHistoryJson> historyByCoin(String idIcon, int timeFrame,int limit,String order) throws CoinException;

}
