package com.mali.exchanger.communication.impl.deal;

import com.mali.entity.Market;
import com.mali.entity.Order;
import com.mali.entity.TradeResult;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealException;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import com.mali.exchanger.communication.exceptions.ExchangerServicesFactoryException;
import com.mali.exchanger.communication.factories.ExchangerServicesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnforcedSellImpl implements Deal {

    @Autowired
    private ExchangerServicesFactory exchangerServicesFactory;

    @Override
    public TradeResult newDeal(Exchanger exchanger, Market market, double quantity, double price, String key, String secret) throws DealException {
        TradeResult tradeResult = new TradeResult();

        int maxAttempts = 0;
        while (!tradeResult.isSuccess() && maxAttempts < 5) {
            try {
                tradeResult = exchangerServicesFactory.getService(exchanger).newDeal(market, DealType.SELL_MARKET,quantity,price,key,secret);
            } catch (ExchangerServicesException e){
            } catch (ExchangerServicesFactoryException e) {
            }
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maxAttempts++;
        }

        //someshit happens
        if (!tradeResult.isSuccess() && maxAttempts == 5)
            throw new DealException("Não foi possivel realizar o deal no exchanger = "+exchanger.toString());

        tradeResult.setType(DealType.SELL_MARKET_ENFORCED);

        maxAttempts = 0;
        TradeResult checkedResult = new TradeResult();
        while (!checkedResult.isSuccess() && maxAttempts < 5){
            try {
                checkedResult = checkDeal(exchanger,tradeResult,market,key,secret);
            } catch (DealException e){
            }
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
            }
            maxAttempts++;
        }

        //someshit happens
        if (!checkedResult.isSuccess() && maxAttempts == 5)
            throw new DealException("Não foi possivel checar o deal no "+exchanger.toString());

        return checkedResult;
    }

    @Override
    public TradeResult checkDeal(Exchanger exchanger, TradeResult tradeResult, Market market, String key, String secret) throws DealException {

        Order details = new Order();

        try {

            details = exchangerServicesFactory.getService(exchanger).orderInfo(market,tradeResult.getUuid(), key, secret);
            try {
                Thread.sleep(5000);
                if (details.isLive()) exchangerServicesFactory.getService(exchanger).orderCancel(details.getUuid(),market,key,secret);
            } catch (Exception e){}
            details = exchangerServicesFactory.getService(exchanger).orderInfo(market,tradeResult.getUuid(), key, secret);
            double fromAmount = 0, toAmount = 0;
            fromAmount = details.getCosts();
            toAmount = details.getQuantity()-details.getRemaining();
            tradeResult.setVolumeToCoin(toAmount);
            tradeResult.setVolumeBaseCoin(fromAmount);
            tradeResult.setPrice(details.getPrice());
            return tradeResult;

        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new DealException("problema ao checar ordem no check deal! no exchanger "+exchanger.toString());
        } catch (ExchangerServicesFactoryException e) {
            e.printStackTrace();
            throw new DealException("problema ao checar ordem no check deal! no exchanger "+exchanger.toString());
        }
    }
}
