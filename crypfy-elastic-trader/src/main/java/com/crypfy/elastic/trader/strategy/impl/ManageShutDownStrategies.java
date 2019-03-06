package com.crypfy.elastic.trader.strategy.impl;

import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.persistance.repository.StrategyRepository;
import com.crypfy.elastic.trader.strategy.StrategyStatusManager;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManageShutDownStrategies implements StrategyStatusManager {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MessageSender msgSender;
    @Autowired
    StrategyRepository strategyRepository;

    @Override
    public void managePossibleStates(Strategy strategy) throws StrategyStatusManagerException {

        List<Order> remainingOrders = orderRepository.findByStrategyNameAndOrderStatusNot(strategy.getName(),OrderStatus.CLOSED);

        if (remainingOrders.size()==0){
            strategy.setStrategyStatus(StrategyStatus.OFF);
            strategyRepository.save(strategy);
            msgSender.sendMsg("Estratégia: "+ strategy.getName()+", finalizada com sucesso!");
        }

    }

}
