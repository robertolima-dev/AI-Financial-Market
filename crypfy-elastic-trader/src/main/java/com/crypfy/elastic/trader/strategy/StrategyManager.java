package com.crypfy.elastic.trader.strategy;

import com.crypfy.elastic.trader.strategy.exceptions.StrategyManagerException;

public interface StrategyManager {

    public void manage() throws StrategyManagerException;

}
