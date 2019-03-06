package com.mali.exchanger.communication.impl.bitfinex.implementations.deal;

import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexApiException;
import com.mali.exchanger.communication.impl.bitfinex.implementations.BitfinexApis;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitfinexBuyDealImpl implements Deal {

    @Autowired
    BitfinexApis bitfinexApis;

    @Override
    public TradeResult newDeal(Exchanger exchanger,Market market, double quantity, double price, String key, String secret) throws DealException {

        TradeResult tradeResult = new TradeResult();

        OrderDetails buyDetails = null;
        try {
            buyDetails = bitfinexApis.getNewLimitOrder(quantity,price,market,"buy",key,secret);
            tradeResult.setPrice(price);
            tradeResult.setSuccess(true);
            tradeResult.setTime(System.currentTimeMillis());
            tradeResult.setType(DealType.BUY);
            tradeResult.setUuid(String.valueOf(buyDetails.getId()));
            tradeResult.setVolumeToCoin(quantity);
            tradeResult.setVolumeBaseCoin(quantity*price);
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao new buy deal do bitfinex",e.getErrors());
        }

        return tradeResult;
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }

}
