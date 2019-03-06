package com.mali.exchanger.communication.impl.okex.implementations.deal;

import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexApiException;
import com.mali.exchanger.communication.impl.okex.implementations.OkexApis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OkexSellDealImpl implements Deal {

    @Autowired
    OkexApis apis;

    @Override
    public TradeResult newDeal(Exchanger exchanger, Market market, double quantity, double price, String key, String secret) throws DealException {
        try {
            com.mali.exchanger.communication.impl.okex.sub.entity.TradeResult okexTradeResult = apis.newSellOrder(price, quantity, market, key, secret);
            TradeResult tradeResult = new TradeResult();
            tradeResult.setVolumeBaseCoin(quantity*price);
            tradeResult.setVolumeToCoin(quantity);
            tradeResult.setUuid(String.valueOf(okexTradeResult.getOrderId()));
            tradeResult.setType(DealType.SELL);
            tradeResult.setTime(new Date().getTime());
            tradeResult.setSuccess(true);
            tradeResult.setPrice(price);
            return tradeResult;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro no new deal (okex sell deal)");
        }
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }
}
