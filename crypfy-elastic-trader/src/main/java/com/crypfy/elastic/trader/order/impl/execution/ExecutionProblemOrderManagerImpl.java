package com.crypfy.elastic.trader.order.impl.execution;

import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.order.OrderExecutionManager;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionProblemOrderManagerImpl implements OrderExecutionManager{

    @Autowired
    MessageSender msgSender;

    @Override
    public void manageOrder(Order order) throws OrderManagerException {

        if ( ((System.currentTimeMillis()/1000 - order.getOpenDate().getTime()/1000) % 60*10) == 0 ) msgSender.sendMsg("Estratégia "+order.getStrategyName()+": Existe uma ordem com problema na execução, por favor verificar!" +
                "\nDetalhes: Exchanger: "+order.getExchanger().toString()+", Mercado: "+order.getFromCoin()+"-"+order.getToCoin());

    }

}
