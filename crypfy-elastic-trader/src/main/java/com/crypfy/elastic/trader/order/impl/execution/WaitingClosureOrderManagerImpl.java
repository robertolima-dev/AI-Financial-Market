package com.crypfy.elastic.trader.order.impl.execution;

import com.crypfy.elastic.trader.order.OrderExecutionManager;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.OrderType;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WaitingClosureOrderManagerImpl implements OrderExecutionManager{

    @Autowired
    TradeServices tradeServices;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public void manageOrder(Order order) throws OrderManagerException {

        try {
            if (tradeServices.closeConditionsMet(order,tradeServices.marketFromOrder(order),order.getExchangerDetails())){
                order.setCloseReason(tradeServices.closeReason(order,tradeServices.marketFromOrder(order),order.getExchangerDetails()));
                order.setOrderStatus( order.isTwoStep() ? OrderStatus.WAITING_TWO_STEP_EXECUTION : OrderStatus.WAITING_DIRECT_EXECUTION );
                order.setOrderType(OrderType.SELL_MARKET_ENFORCED);
                orderRepository.save(order);
            }
        } catch (TradeException e){
            e.printStackTrace();
            throw new OrderManagerException("Erro ao tentar checar condicoes de fechamento",e.getErrors(),e.getStatus());
        }


    }

}
