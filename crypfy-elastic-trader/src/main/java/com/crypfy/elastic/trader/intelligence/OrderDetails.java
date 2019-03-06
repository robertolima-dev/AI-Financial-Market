package com.crypfy.elastic.trader.intelligence;

import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.trade.json.RequirementsResponse;

public interface OrderDetails {

    public Order setIt(Order order, Strategy strategy, SubStrategy details, double totalBalance, ExchangerDetails exchanger, int nExchangers);

}
