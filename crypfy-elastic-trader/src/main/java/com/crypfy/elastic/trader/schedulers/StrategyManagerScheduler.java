package com.crypfy.elastic.trader.schedulers;

import com.crypfy.elastic.trader.order.OrderManager;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.strategy.StrategyManager;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

@Component
public class StrategyManagerScheduler {

    @Autowired
    private StrategyManager strategyManager;

    @Scheduled(fixedDelay = 1000*60*6)
    public void manageStrategies() {
        try {
            strategyManager.manage();
        } catch (StrategyManagerException e) {
            e.printStackTrace();
        }
    }

}
