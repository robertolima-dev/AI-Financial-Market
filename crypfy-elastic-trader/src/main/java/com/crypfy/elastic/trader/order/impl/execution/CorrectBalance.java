package com.crypfy.elastic.trader.order.impl.execution;

import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorrectBalance {

    @Autowired
    TradeServices tradeServices;

    public Double correctBuyBalance(Order order) throws OrderManagerException {

        try {
            //available base balance
            double availableBalance = tradeServices.coinBalance(order.getExchangerDetails(),order.getFromCoin());
            if (order.getOrderAmount().doubleValue() < availableBalance) return order.getOrderAmount().doubleValue();
            if (order.getOrderAmount().doubleValue() >= availableBalance) {
                if (order.getOrderAmount().doubleValue() > availableBalance*1.4) {
                    throw new OrderManagerException("Saldo disponivel pra transação é muito pequeno comparado ao que a ordem pede!");
                } else return availableBalance*0.995;

            } else throw new OrderManagerException("Saldo disponivel pra transação é muito pequeno comparado ao que a ordem pede!");
        } catch (TradeException e){
            throw new OrderManagerException("Problema tentar checar order correct balance",e.getErrors(),e.getStatus());
        }
    }

    public Double correctSellBalance(Order order) throws OrderManagerException {

        try {
            //available base balance
            double availableBalance = tradeServices.coinBalance(order.getExchangerDetails(),order.getToCoin());
            if (order.getOrderAmount().doubleValue() <= availableBalance) {
                if ( availableBalance > order.getOrderAmount().doubleValue()*1.02 ){
                    return order.getOrderAmount().doubleValue();
                } else {
                    return availableBalance;
                }
            } else {
                return availableBalance;
            }
        } catch (TradeException e){
            throw new OrderManagerException("Problema tentar checar order correct balance",e.getErrors(),e.getStatus());
        }
    }

}
