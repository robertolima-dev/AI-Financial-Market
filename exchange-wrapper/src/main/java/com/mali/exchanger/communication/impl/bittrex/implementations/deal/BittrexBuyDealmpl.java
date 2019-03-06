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
public class BittrexBuyDealmpl implements Deal {

    @Autowired
    private BittrexApis apis;

    @Override
    public TradeResult newDeal(Exchanger exchanger, Market market, double quantity, double price, String key, String secret) throws DealException {

        TradeResult tradeResult = new TradeResult();
        DealStatus buyStatus = null;
        try {
            buyStatus = apis.marketBuy(quantity/price,price,market,key,secret);
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao buy deal do bittrex",e.getErrors());
        }
        if(buyStatus.isSuccess()) {

            tradeResult.setPrice(price);
            tradeResult.setSuccess(buyStatus.isSuccess());
            tradeResult.setTime(System.currentTimeMillis());
            tradeResult.setType(DealType.BUY);
            tradeResult.setUuid(buyStatus.getResult().getUuid());
            tradeResult.setVolumeToCoin((quantity/tradeResult.getPrice()));
            tradeResult.setVolumeBaseCoin(quantity);
            tradeResult.setMsg(buyStatus.getMessage());
        }else{
            tradeResult.setMsg(buyStatus.getMessage());
        }
        return tradeResult;
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) {
        //not needed here
        return null;
    }
}
