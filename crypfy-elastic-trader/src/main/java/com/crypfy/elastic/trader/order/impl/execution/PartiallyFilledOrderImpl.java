package com.crypfy.elastic.trader.order.impl.execution;

import com.crypfy.elastic.trader.order.OrderExecutionManager;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartiallyFilledOrderImpl implements OrderExecutionManager{

    @Autowired
    OrderRepository orderRepository;

    @Override
    public void manageOrder(Order order) throws OrderManagerException {


    }
}
