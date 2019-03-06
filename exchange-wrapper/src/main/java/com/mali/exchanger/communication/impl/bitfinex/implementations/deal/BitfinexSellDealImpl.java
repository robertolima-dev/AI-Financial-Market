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
public class BitfinexSellDealImpl implements Deal {

    @Autowired
    BitfinexApis bitfinexApis;

    @Override
    public TradeResult newDeal(Exchanger exchanger, Market market, double quantity, double price, String key, String secret) throws DealException {

        TradeResult tradeResult = new TradeResult();

        OrderDetails sellDetails = null;
        try {
            sellDetails = bitfinexApis.getNewLimitOrder(quantity,price,market,"sell",key,secret);
            tradeResult.setPrice(price);
            tradeResult.setSuccess(sellDetails.isLive());
            tradeResult.setTime(System.currentTimeMillis());
            tradeResult.setType(DealType.SELL);
            tradeResult.setUuid(String.valueOf(sellDetails.getId()));
            tradeResult.setVolumeToCoin(quantity);
            tradeResult.setVolumeBaseCoin(quantity*price);
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao new sell deal do bitfinex",e.getErrors());
        }

        return tradeResult;
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }

}
