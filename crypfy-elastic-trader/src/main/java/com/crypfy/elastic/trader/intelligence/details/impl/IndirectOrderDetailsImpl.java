package com.crypfy.elastic.trader.intelligence.details.impl;

import com.crypfy.elastic.trader.intelligence.OrderDetails;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.OrderType;
import com.crypfy.elastic.trader.trade.json.RequirementsResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class IndirectOrderDetailsImpl implements OrderDetails {

    @Override
    public Order setIt(Order order, Strategy strategy, SubStrategy details, double totalBalance, ExchangerDetails exchanger, int nExchangers) {

        double balance = (totalBalance / (details.getAllowedSimultaneousTransaction()*nExchangers));
        order.setId(null);
        order.setInitialBaseAmount(new BigDecimal(balance).setScale(8, RoundingMode.DOWN));
        order.setOrderAmount(order.getInitialBaseAmount().setScale(8, RoundingMode.DOWN));
        order.setStrategyName(strategy.getName());
        order.setExchanger(exchanger.getExchanger());
        order.setExchangerDetails(exchanger);
        order.setMode(strategy.getMode());
        order.setTimeFrame(details.getTimeFrame());
        order.setPriority(details.getPriority());
        order.setFromCoin(strategy.getBaseCurrency().toString());
        order.setToCoin("BTC");
        order.setOrderStatus(OrderStatus.WAITING_TWO_STEP_EXECUTION);
        order.setTwoStep(true);
        order.setOrderType( (order.getOrderType().equals(OrderType.BUY) || order.getOrderType().equals(OrderType.BUY_MARKET) || order.getOrderType().equals(OrderType.BUY_MARKET_ENFORCED)) ? OrderType.BUY_MARKET_ENFORCED : OrderType.SELL_MARKET_ENFORCED );
        order.setIntelligenceType(details.getIntelligenceType());
        order.setOpenDate(new Date());
        order.setSubStrategyName(details.getName());
        order.setBaseCurrency(strategy.getBaseCurrency());
        order.setTpSlNeeded(details.isUsingTPSL());
        order.setAlreadyExec(new BigDecimal(0));

        return order;
    }
}
