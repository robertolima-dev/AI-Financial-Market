package com.crypfy.elastic.trader.order.factories;

import com.crypfy.elastic.trader.order.OrderModeManager;
import com.crypfy.elastic.trader.order.exceptions.ModeFactoryException;
import com.crypfy.elastic.trader.order.impl.mode.LiveOrderManagerImpl;
import com.crypfy.elastic.trader.order.impl.mode.TestOrderManagerImpl;
import com.crypfy.elastic.trader.persistance.enums.StrategyMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderModeManagerFactory {

    @Autowired
    LiveOrderManagerImpl liveOrderManager;
    @Autowired
    TestOrderManagerImpl testOrderManager;

    public OrderModeManager getImpl(StrategyMode mode) throws ModeFactoryException {

        if (mode.equals(StrategyMode.LIVE)) return liveOrderManager;

        if (mode.equals(StrategyMode.TEST)) return testOrderManager;

        throw new ModeFactoryException("Implementação de order mode não encontrada!");
    }

}
