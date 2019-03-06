package com.crypfy.elastic.trader.integrations.mali.coin.data;

import com.crypfy.elastic.trader.integrations.mali.coin.data.exception.CoinException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinHistoryJson;
import java.util.List;

public interface CoinHistoryService {

    public List<CoinHistoryJson> historyByCoin(String idIcon,int timeFrame,int limit) throws CoinException;

}
