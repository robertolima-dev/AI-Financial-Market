package com.crypfy.elastic.trader.strategy;

import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyException;

import java.util.List;

public interface StrategyServices {

    public StrategyStatus newStrategy(Strategy strategy) throws StrategyException;
    public StrategyStatus closeStrategy(String strategyName) throws StrategyException;
    public StrategyStatus closeStrategyNow(String strategyName) throws StrategyException;
    public List<Strategy> list(String strategyName);
    public SubStrategy subStrategyByIntelligenceType(Strategy strategy, IntelligenceType type) throws StrategyException;

}
