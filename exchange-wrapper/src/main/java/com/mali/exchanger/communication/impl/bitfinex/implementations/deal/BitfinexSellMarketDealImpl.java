package com.mali.exchanger.communication.impl.bitfinex.implementations.deal;

import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.exceptions.DealPriceException;
import com.mali.exchanger.communication.exceptions.DealPriceFactoryException;
import com.mali.exchanger.communication.factories.DealPriceFactory;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexApiException;
import com.mali.exchanger.communication.impl.bitfinex.implementations.BitfinexApis;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitfinexSellMarketDealImpl implements Deal {

    @Autowired
    BitfinexApis bitfinexApis;
    @Autowired
    DealPriceFactory sellDealPrice;

    @Override
    public TradeResult newDeal(Exchanger exchanger,Market market, double quantity, double price, String key, String secret) throws DealException {

        TradeResult tradeResult = new TradeResult();

        try {
            OrderDetails details = bitfinexApis.getNewMarketOrder(quantity,market,"sell",key,secret);
            tradeResult.setPrice(details.getPrice());
            tradeResult.setSuccess(true);
            tradeResult.setTime(System.currentTimeMillis());
            tradeResult.setType(DealType.SELL_MARKET);
            tradeResult.setUuid(String.valueOf(details.getId()));
            tradeResult.setVolumeToCoin(quantity);
            tradeResult.setVolumeBaseCoin(quantity*details.getPrice());
            return tradeResult;
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao new sell market deal do bitfinex (bitfinex api)",e.getErrors());
        }
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }

}
