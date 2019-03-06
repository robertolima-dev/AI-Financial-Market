package com.crypfy.elastic.trader.order.impl.mode;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.DealRequest;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.TradeResult;
import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.order.OrderModeManager;
import com.crypfy.elastic.trader.order.exceptions.OrderModeManagerException;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LiveOrderManagerImpl implements OrderModeManager {

    @Autowired
    ExchangerServices exchangerServices;
    @Autowired
    MessageSender msgSender;

    @Override
    public TradeResult manageOrderMode(ExchangerDetails exchanger, Market market, DealRequest dealRequest) throws OrderModeManagerException {

        try {
            return exchangerServices.newOrder(exchanger.getExchanger(),market,dealRequest,exchanger.getKey(),exchanger.getSecret());
        } catch (ExchangerException e){
            e.printStackTrace();
            msgSender.sendMsg("Problema ao abrir ordem com as seguintes variaveis:" +
                    "\nExchanger: "+exchanger.getExchanger().toString() +
                    "\nMercado: "+market.getMarketSymbol() +
                    "\nPor favor verifique!!!");
            throw new OrderModeManagerException("Erro ao executar enforced order!",e.getErrors(),e.getStatus());
        }

    }
}
