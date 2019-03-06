package com.crypfy.elastic.trader.order.impl.mode;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.DealRequest;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.TradeResult;
import com.crypfy.elastic.trader.order.OrderModeManager;
import com.crypfy.elastic.trader.order.exceptions.OrderModeManagerException;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestOrderManagerImpl implements OrderModeManager {

    @Autowired
    ExchangerServices exchangerServices;

    @Override
    public TradeResult manageOrderMode(ExchangerDetails exchanger, Market market, DealRequest dealRequest) throws OrderModeManagerException {

        TradeResult tradeResult = new TradeResult();

        try {
            tradeResult.setPrice(exchangerServices.dealPrice(exchanger.getExchanger(),market,dealRequest.getDealType().toString(),dealRequest.getQuantity()).doubleValue());
            tradeResult.setSuccess(true);
            tradeResult.setTime(new Date().getTime());
            tradeResult.setUuid("VIRTUAL ORDER");
            tradeResult.setVolumeBaseCoin(dealRequest.getQuantity()*tradeResult.getPrice());
            tradeResult.setVolumeToCoin(dealRequest.getQuantity());
            return tradeResult;
        } catch (ExchangerException e){
            e.printStackTrace();
            throw new OrderModeManagerException("Erro ao consultar preço para ordem virtual",e.getErrors(),e.getStatus());
        }


    }
}
