package com.crypfy.elastic.trader.strategy.impl;

import com.crypfy.elastic.trader.intelligence.exceptions.OppSearcherFactoryException;
import com.crypfy.elastic.trader.intelligence.exceptions.OrderOppSaverException;
import com.crypfy.elastic.trader.intelligence.factories.SearchOrdersIntelligenceTypeFactory;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.strategy.StrategyStatusManager;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyStatusManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManageOnStrategies implements StrategyStatusManager {

    @Autowired
    SearchOrdersIntelligenceTypeFactory typeFactory;

    @Override
    public void managePossibleStates(Strategy strategy) throws StrategyStatusManagerException {

        try {
            for (SubStrategy subStrategy : strategy.getSubStrategies()){

                // if (some time control needed)

                typeFactory.getImpl(subStrategy.getIntelligenceType()).saveOrderOpportunities(strategy,subStrategy);

            }
        } catch (OppSearcherFactoryException e) {
            e.printStackTrace();
            throw new StrategyStatusManagerException("Erro ao controlar estratégia com execution 'ON'",e.getErrors(),e.getStatus());
        } catch (OrderOppSaverException e) {
            e.printStackTrace();
            throw new StrategyStatusManagerException("Erro ao controlar estratégia com execution 'ON'",e.getErrors(),e.getStatus());
        }

    }
}
