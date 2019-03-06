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
public class BitfinexBuyMarketDealImpl implements Deal {

    @Autowired
    BitfinexApis bitfinexApis;
    @Autowired
    DealPriceFactory buyDealPrice;

    @Override
    public TradeResult newDeal(Exchanger exchanger,Market market, double quantity, double price, String key, String secret) throws DealException {
        TradeResult tradeResult = new TradeResult();

        try {
            double correctQuantity = quantity/buyDealPrice.getService(DealType.BUY).dealPrice(market,exchanger,quantity);
            OrderDetails details = bitfinexApis.getNewMarketOrder(correctQuantity,market,"buy",key,secret);
            tradeResult.setPrice(details.getPrice());
            tradeResult.setSuccess(true);
            tradeResult.setTime(System.currentTimeMillis());
            tradeResult.setType(DealType.BUY_MARKET);
            tradeResult.setUuid(String.valueOf(details.getId()));
            tradeResult.setVolumeToCoin(details.getOriginalAmount());
            tradeResult.setVolumeBaseCoin(details.getOriginalAmount()*details.getPrice());
            return tradeResult;
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao new buy market deal do bitfinex (bitfinex api)",e.getErrors());
        } catch (DealPriceFactoryException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao new buy market deal do bitfinex (deal price factory)",e.getErrors());
        } catch (DealPriceException e) {
            e.printStackTrace();
            throw new DealException("Erro na implementaçao new buy market deal do bitfinex (deal price)",e.getErrors());
        }

    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) {
        //not needed here
        return null;
    }
}
