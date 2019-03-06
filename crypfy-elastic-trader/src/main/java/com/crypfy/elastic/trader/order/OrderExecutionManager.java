package com.crypfy.elastic.trader.order;

import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.persistance.entity.Order;

public interface OrderExecutionManager {

    public void manageOrder(Order order) throws OrderManagerException;

}
