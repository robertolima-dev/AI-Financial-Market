package com.crypfy.elastic.trader.strategy.impl;

import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.enums.OrderCloseReason;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.OrderType;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.persistance.repository.StrategyRepository;
import com.crypfy.elastic.trader.strategy.StrategyStatusManager;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManageShutDownImmStrategies implements StrategyStatusManager {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MessageSender msgSender;
    @Autowired
    StrategyRepository strategyRepository;

    @Override
    public void managePossibleStates(Strategy strategy) throws StrategyStatusManagerException {

        List<Order> remainingOrders = orderRepository.findByStrategyNameAndOrderStatus(strategy.getName(), OrderStatus.WAITING_CLOSURE_CONDITIONS);

        for (Order order : remainingOrders) {
            order.setCloseReason(OrderCloseReason.CLOSED_BY_SHUT_DOWN_IMMEDIATELY_ORDER);
            order.setOrderStatus(order.isTwoStep() ? OrderStatus.WAITING_TWO_STEP_EXECUTION : OrderStatus.WAITING_DIRECT_EXECUTION);
            order.setOrderType(OrderType.SELL);
            orderRepository.save(order);
        }
        if (remainingOrders.size() == 0){
            strategy.setStrategyStatus(StrategyStatus.OFF);
            strategyRepository.save(strategy);
            msgSender.sendMsg("Estratégia: "+ strategy.getName()+", finalizada com sucesso!");
        }

    }
}
