package com.mali.exchanger.communication.impl.okex.implementations.deal;

import com.mali.entity.Market;
import com.mali.entity.Order;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexApiException;
import com.mali.exchanger.communication.impl.okex.implementations.OkexApis;
import com.mali.exchanger.communication.impl.okex.implementations.OkexExchangerServicesImpl;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OkexSellMarketImpl implements Deal {

    @Autowired
    OkexApis apis;
    @Autowired
    OkexExchangerServicesImpl okexExchangerServices;

    @Override
    public TradeResult newDeal(Exchanger exchanger,Market market, double quantity, double price, String key, String secret) throws DealException {
        try {
            com.mali.exchanger.communication.impl.okex.sub.entity.TradeResult okexTradeResult = apis.newSellMarketOrder(quantity, market, key, secret);
            TradeResult tradeResult = new TradeResult();
            Order order = okexExchangerServices.orderInfo(market,String.valueOf(okexTradeResult.getOrderId()),key,secret);
            tradeResult.setVolumeBaseCoin(order.getCosts());
            tradeResult.setVolumeToCoin(quantity);
            tradeResult.setUuid(String.valueOf(okexTradeResult.getOrderId()));
            tradeResult.setType(DealType.SELL_MARKET);
            tradeResult.setTime(new Date().getTime());
            tradeResult.setSuccess(true);
            tradeResult.setPrice(order.getPrice());
            return tradeResult;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new DealException("Problema no new deal (okex new sell market)");
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new DealException("Problema no new deal (okex new sell market [order info] )");
        }
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger, TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }
}
