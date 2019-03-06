package com.mali.exchanger.communication.impl.binance.implementations.deal;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.api.DealPrice;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.exceptions.DealPriceException;
import com.mali.exchanger.communication.exceptions.DealPriceFactoryException;
import com.mali.exchanger.communication.factories.DealPriceFactory;
import com.mali.exchanger.communication.impl.binance.implementations.BinanceExchangeServicesImpl;
import com.mali.exchanger.communication.impl.binance.implementations.stepSize.StepSizeController;
import com.mali.exchanger.communication.impl.binance.sub.entity.MarketDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;

@Service
public class BinanceBuyMarketDealImpl implements Deal {

    @Autowired
    private DealPriceFactory dealPriceFactory;
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
            price = dealPriceFactory.getService(DealType.BUY).dealPrice(market,exchanger,quantity);
            MarketDetails details = binanceExchangeServices.getMarketDetails(correctMarket);
            BigDecimal correctPrice = new BigDecimal(quantity/price).setScale(8,BigDecimal.ROUND_HALF_DOWN);
            double correctQty = stepSizeController.correctQuantity(correctPrice.doubleValue(),details.getQuantityStep(),details.getQuantityMinSize());
            NewOrderResponse newOrderResponse = client.newOrder(marketBuy(correctMarket,String.valueOf(correctQty)));
            tradeResult.setSuccess(true);
            tradeResult.setTime(new Date().getTime());
            tradeResult.setType(DealType.BUY_MARKET);
            tradeResult.setUuid(newOrderResponse.getClientOrderId());
            tradeResult.setVolumeToCoin(correctQty/Double.valueOf(newOrderResponse.getPrice()));
            tradeResult.setVolumeBaseCoin(correctQty);
            tradeResult.setPrice(price);
            return tradeResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DealException("Problema no buy market deal do binance!");
        }
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger, TradeResult tradeResult, Market market, String key, String secret) throws DealException {
        //not needed here
        return null;
    }
}
