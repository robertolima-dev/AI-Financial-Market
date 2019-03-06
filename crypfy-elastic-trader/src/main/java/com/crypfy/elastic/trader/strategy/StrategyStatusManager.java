package com.crypfy.elastic.trader.strategy;

import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusManagerException;

public interface StrategyStatusManager {

    public void managePossibleStates(Strategy strategy) throws StrategyStatusManagerException;
}
