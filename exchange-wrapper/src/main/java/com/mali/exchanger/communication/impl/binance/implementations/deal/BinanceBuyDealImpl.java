package com.mali.exchanger.communication.impl.binance.implementations.deal;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.impl.binance.implementations.BinanceExchangeServicesImpl;
import com.mali.exchanger.communication.impl.binance.implementations.stepSize.StepSizeController;
import com.mali.exchanger.communication.impl.binance.sub.entity.MarketDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.binance.api.client.domain.account.NewOrder.limitBuy;

@Service
public class BinanceBuyDealImpl implements Deal {

    @Autowired
    private BinanceExchangeServicesImpl binanceExchangeServices;
    @Autowired
    private StepSizeController stepSizeController;

    @Override
    public TradeResult newDeal(Exchanger exchanger, Market market, double quantity, double price, String key, String secret) throws DealException {
        try {
            BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(key,secret);
            BinanceApiRestClient client = factory.newRestClient();
            TradeResult tradeResult = new TradeResult();
            String correctMarket = market.getToCoin()+market.getBaseCoin();
            MarketDetails details = binanceExchangeServices.getMarketDetails(correctMarket);
            double correctQuantity = stepSizeController.correctQuantity(quantity,details.getQuantityStep(),details.getQuantityMinSize());
            double correctPrice = stepSizeController.correctQuantity(price,details.getPriceStep(),details.getPriceMinSize());
            NewOrderResponse newOrderResponse = client.newOrder(limitBuy(correctMarket, TimeInForce.GTC, String.valueOf(correctQuantity), String.valueOf(correctPrice)));
            tradeResult.setSuccess(true);
            tradeResult.setTime(new Date().getTime());
            tradeResult.setType(DealType.BUY);
            tradeResult.setUuid(newOrderResponse.getClientOrderId());
            tradeResult.setVolumeToCoin(correctQuantity);
            tradeResult.setVolumeBaseCoin(correctQuantity*correctPrice);
            tradeResult.setPrice(price);
            return tradeResult;
        } catch (Exception e){
            e.printStackTrace();
            throw new DealException("Problema no buy deal do binance!");
        }
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger, TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }
}
