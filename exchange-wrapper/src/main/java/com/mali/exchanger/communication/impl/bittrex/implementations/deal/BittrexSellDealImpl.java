package com.mali.exchanger.communication.impl.bittrex.implementations.deal;

import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexApiException;
import com.mali.exchanger.communication.impl.bittrex.implementations.BittrexApis;
import com.mali.exchanger.communication.impl.bittrex.sub.entity.DealStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BittrexSellDealImpl implements Deal {

    @Autowired
    private BittrexApis apis;

    @Override
    public TradeResult newDeal(Exchanger exchanger, Market market, double quantity, double price, String key, String secret) throws DealException {

        TradeResult tradeResult = new TradeResult();
        DealStatus sellStatus = null;
        try {
            sellStatus = apis.marketSell(quantity,price,market,key,secret);
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao sell deal do bittrex",e.getErrors());
        }
        if(sellStatus.isSuccess()) {
            tradeResult.setPrice(price);
            tradeResult.setSuccess(sellStatus.isSuccess());
            tradeResult.setTime(System.currentTimeMillis());
            tradeResult.setType(DealType.SELL);
            tradeResult.setUuid(sellStatus.getResult().getUuid());
            tradeResult.setVolumeToCoin(quantity);
            tradeResult.setVolumeBaseCoin(tradeResult.getVolumeBaseCoin());
            tradeResult.setMsg(sellStatus.getMessage());
        }else{
            tradeResult.setMsg(sellStatus.getMessage());
        }
        return tradeResult;
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) {
        //not needed here
        return null;
    }
}
