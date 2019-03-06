package com.crypfy.elastic.trader.order;

import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;

import java.util.List;

public interface OrderManager {

    public void manage() throws OrderManagerException;
    public List<String> distinctCallCoinsByStrategyNameAndSubStrategyNameAndOrderStatusNot(String strategyName, String strategyDetailName);

}
