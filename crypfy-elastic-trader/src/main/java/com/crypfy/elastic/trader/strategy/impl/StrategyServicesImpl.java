package com.crypfy.elastic.trader.strategy.impl;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.persistance.repository.StrategyRepository;
import com.crypfy.elastic.trader.strategy.StrategyServices;
import com.crypfy.elastic.trader.strategy.exceptions.StrategyException;
import com.crypfy.elastic.trader.strategy.factories.StrategyStatusManagerFactory;
import com.crypfy.elastic.trader.trade.TradeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyServicesImpl implements StrategyServices {

    @Autowired
    StrategyRepository strategyRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MessageSender msgSender;
    @Autowired
    TradeServices tradeServices;
    @Autowired
    ExchangerServices exchangerServices;
    @Autowired
    StrategyStatusManagerFactory strategyStatusManagerFactory;

    @Override
    public StrategyStatus newStrategy(Strategy strategy) throws StrategyException {
        try {
            for (SubStrategy details : strategy.getSubStrategies()) details.setName(details.getIntelligenceType().toString()+"-"+details.getTimeFrame().toString()+",SubStrategyNumber="+strategy.getSubStrategies().indexOf(details));
            strategy.setStrategyStatus(StrategyStatus.ON);
            strategyRepository.save(strategy);
            msgSender.sendMsg("Uma nova estratégia foi adicionada. Nome: "+ strategy.getName());
            return strategy.getStrategyStatus();
        } catch (Exception e){
            msgSender.sendMsg("Ocorreu um erro ao salvar a nova estratégia: "+ strategy.getName());
            throw new StrategyException("Erro ao salvar nova estrategia",e.hashCode());
        }
    }

    @Override
    public StrategyStatus closeStrategy(String strategyName) throws StrategyException {
        try {
            Strategy strategy = strategyRepository.findByName(strategyName);
            strategy.setStrategyStatus(StrategyStatus.SHUT_DOWN);
            strategyRepository.save(strategy);
            msgSender.sendMsg("A estratégia: "+ strategy.getName()+", será drenada e finalizada em breve!");
            return strategy.getStrategyStatus();
        } catch (Exception e){
            msgSender.sendMsg("Não foi possível mudar o execution para 'SHUT_DOWN' da estratégia: "+strategyName);
            throw new StrategyException("Erro ao tentar trocar execution da estratégia para 'shut_down' ",e.hashCode());
        }
    }

    @Override
    public StrategyStatus closeStrategyNow(String strategyName) throws StrategyException {
        try {
            Strategy strategy = strategyRepository.findByName(strategyName);
            strategy.setStrategyStatus(StrategyStatus.SHUT_DOWN_IMMEDIATELY);
            strategyRepository.save(strategy);
            msgSender.sendMsg("A estratégia: "+ strategy.getName()+", está sendo finalizada!");
            return strategy.getStrategyStatus();
        } catch (Exception e){
            msgSender.sendMsg("Não foi possível mudar o execution para 'SHUT_DOWN_IMMEDIATELY' da estratégia: "+strategyName);
            throw new StrategyException("Erro ao tentar trocar execution da estratégia para 'shut_down_immediately' ",e.hashCode());
        }
    }

    @Override
    public List<Strategy> list(String strategyName) {
        return null;
    }

    @Override
    public SubStrategy subStrategyByIntelligenceType(Strategy strategy, IntelligenceType type) throws StrategyException {

        for (SubStrategy subStrategy : strategy.getSubStrategies()) {
            if (subStrategy.getIntelligenceType().equals(type)) return subStrategy;
        }

        throw new StrategyException("Strategy details não encontrado!");

    }

}
