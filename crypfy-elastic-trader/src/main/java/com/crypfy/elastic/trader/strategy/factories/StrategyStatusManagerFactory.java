package com.crypfy.elastic.trader.strategy.factories;

import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.strategy.StrategyStatusManager;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusFactoryException;
import com.crypfy.elastic.trader.strategy.impl.ManageOnStrategies;
import com.crypfy.elastic.trader.strategy.impl.ManageShutDownImmStrategies;
import com.crypfy.elastic.trader.strategy.impl.ManageShutDownStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyStatusManagerFactory {

    @Autowired
    ManageOnStrategies manageOnStrategies;
    @Autowired
    ManageShutDownStrategies manageShutDownStrategies;
    @Autowired
    ManageShutDownImmStrategies manageShutDownImmStrategies;

    public StrategyStatusManager getImpl(StrategyStatus strategyStatus) throws StrategyStatusFactoryException {

        switch (strategyStatus){
            case ON:
                return manageOnStrategies;
            case SHUT_DOWN:
                return manageShutDownStrategies;
            case SHUT_DOWN_IMMEDIATELY:
                return manageShutDownImmStrategies;
            default:
                throw new StrategyStatusFactoryException("Implementação não encontrada!!!");
        }
    }

}
