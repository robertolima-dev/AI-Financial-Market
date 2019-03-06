package com.crypfy.elastic.trader.strategy.impl;

import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.persistance.repository.StrategyRepository;
import com.crypfy.elastic.trader.strategy.StrategyManager;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyManagerException;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusFactoryException;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusManagerException;
import com.crypfy.elastic.trader.strategy.factories.StrategyStatusManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyManagerImpl implements StrategyManager {

    @Autowired
    StrategyRepository strategyRepository;
    @Autowired
    StrategyStatusManagerFactory statusManagerFactory;

    @Override
    public void manage() throws StrategyManagerException {

        List<Strategy> strategiesToCheck = strategyRepository.findByStrategyStatusNot(StrategyStatus.OFF);

        for (Strategy strategy : strategiesToCheck) {
            try {
                statusManagerFactory.getImpl(strategy.getStrategyStatus()).managePossibleStates(strategy);
            } catch (StrategyStatusManagerException e) {
                e.printStackTrace();
                throw  new StrategyManagerException("Erro ao controlar strategy",e.getErrors(),e.getStatus());
            } catch (StrategyStatusFactoryException e) {
                e.printStackTrace();
                throw  new StrategyManagerException("Erro na strategy factory manager",e.getErrors(),e.getStatus());
            }
        }

    }
}
