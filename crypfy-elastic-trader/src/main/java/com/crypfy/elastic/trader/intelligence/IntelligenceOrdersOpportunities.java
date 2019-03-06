package com.crypfy.elastic.trader.intelligence;

import com.crypfy.elastic.trader.intelligence.exceptions.OrderSearcherException;
import com.crypfy.elastic.trader.intelligence.exceptions.OrderOppSaverException;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;

import java.util.List;

public interface IntelligenceOrdersOpportunities {

    List<Order> searchForOrders(int timeFrame, String name) throws OrderSearcherException;
    public void saveOrderOpportunities(Strategy strategy, SubStrategy details) throws OrderOppSaverException;

}
