package com.crypfy.elastic.trader.intelligence.intelligence.source;

import com.crypfy.elastic.trader.intelligence.exceptions.OrderSearcherException;
import com.crypfy.elastic.trader.intelligence.IntelligenceOrdersOpportunities;
import com.crypfy.elastic.trader.intelligence.exceptions.OrderOppSaverException;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapPortfolioOrdersImpl implements IntelligenceOrdersOpportunities {


    @Override
    public List<Order> searchForOrders(int timeFrame, String name) throws OrderSearcherException {
        return null;
    }

    @Override
    public void saveOrderOpportunities(Strategy strategy, SubStrategy details) throws OrderOppSaverException {

    }
}
