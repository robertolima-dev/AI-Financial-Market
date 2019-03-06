package com.crypfy.elastic.trader.integrations.deep.clustering;

import com.crypfy.elastic.trader.integrations.deep.clustering.exception.ClusterException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinHistoryJson;
import com.crypfy.elastic.trader.persistance.entity.Order;
import java.util.List;

public interface ClusterServices {

    public List<Order> checkForPatterns(String clusterName) throws ClusterException;

}
