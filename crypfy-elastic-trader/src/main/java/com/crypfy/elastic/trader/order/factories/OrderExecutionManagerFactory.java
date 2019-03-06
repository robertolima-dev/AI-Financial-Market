package com.crypfy.elastic.trader.order.factories;

import com.crypfy.elastic.trader.order.OrderExecutionManager;
import com.crypfy.elastic.trader.order.exceptions.OrderExecutionFactoryException;
import com.crypfy.elastic.trader.order.impl.execution.*;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderExecutionManagerFactory {

    @Autowired
    DirectBuyOrderManagerImpl directBuyOrderManagerImpl;
    @Autowired
    DirectSellOrderManagerImpl directSellOrderManagerImpl;
    @Autowired
    IndirectBuyOrderImpl indirectBuyOrder;
    @Autowired
    IndirectSellOrderImpl indirectSellOrder;
    @Autowired
    WaitingClosureOrderManagerImpl waitingClosureOrderManager;
    @Autowired
    ExecutionProblemOrderManagerImpl executionProblemOrderManager;
    @Autowired
    PartiallyFilledOrderImpl partiallyFilledOrder;

    public OrderExecutionManager getImpl(OrderStatus status, OrderType type) throws OrderExecutionFactoryException {

        if (status.equals(OrderStatus.WAITING_DIRECT_EXECUTION)){

            if (type.equals(OrderType.SELL_MARKET_ENFORCED)) return directSellOrderManagerImpl;

            if (type.equals(OrderType.BUY_MARKET_ENFORCED)) return directBuyOrderManagerImpl;

        }

        if (status.equals(OrderStatus.WAITING_TWO_STEP_EXECUTION)){

            if (type.equals(OrderType.SELL_MARKET_ENFORCED)) return indirectSellOrder;

            if (type.equals(OrderType.BUY_MARKET_ENFORCED)) return indirectBuyOrder;

        }

        if (status.equals(OrderStatus.WAITING_CLOSURE_CONDITIONS)) return waitingClosureOrderManager;

        if (status.equals(OrderStatus.EXECUTION_PROBLEM)) return executionProblemOrderManager;

        if (status.equals(OrderStatus.PARTIALLY_FILLED)) return partiallyFilledOrder;

        throw new OrderExecutionFactoryException("Implementação de Order Execution não encontrada!");

    }

}
