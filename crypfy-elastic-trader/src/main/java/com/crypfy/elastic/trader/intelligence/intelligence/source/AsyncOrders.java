package com.crypfy.elastic.trader.intelligence.intelligence.source;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.intelligence.exceptions.OppSearcherFactoryException;
import com.crypfy.elastic.trader.intelligence.factories.SetOrderExecutionFactory;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncOrders {

    @Autowired
    TradeServices tradeServices;
    @Autowired
    private SetOrderExecutionFactory setOrderFactory;
    @Autowired
    private OrderRepository orderRepository;

    //@Async("strat-exec")
    public void checkAndSaveOrdersByExchanger(Order order, Strategy strategy, SubStrategy subStrategy, double balance, ExchangerDetails exchanger, Market market, int nExchangers) throws TradeException, OppSearcherFactoryException {

        boolean orderProcessed = false;

        //set order details
        if (tradeServices.exchangeHasMarket(market, exchanger)) {
            Order directOrder = order;
            directOrder = setOrderFactory.getImpl(OrderStatus.WAITING_DIRECT_EXECUTION).setIt(directOrder,strategy,subStrategy,balance,exchanger,nExchangers);
            if (tradeServices.minimumConditionsMet(directOrder,subStrategy,exchanger)){
                orderRepository.save(directOrder);
                orderProcessed = true;
            }
        }
        if (tradeServices.exchangeHasCoin(market, exchanger) && !orderProcessed) {
            Order twoStepOrder = order;
            twoStepOrder = setOrderFactory.getImpl(OrderStatus.WAITING_TWO_STEP_EXECUTION).setIt(twoStepOrder,strategy,subStrategy,balance,exchanger,nExchangers);
            if (tradeServices.minimumConditionsMet(twoStepOrder,subStrategy,exchanger)) {
                orderRepository.save(twoStepOrder);
            }
        }

    }

}
