package com.mali.exchanger.communication.impl.okex.factories;

import com.mali.exchanger.communication.impl.okex.OkexOrderBook;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexFactoryException;
import com.mali.exchanger.communication.impl.okex.implementations.orderbook.OkexBuyOrderBook;
import com.mali.exchanger.communication.impl.okex.implementations.orderbook.OkexSellOrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OkexOrderBookFactory {

    @Autowired
    OkexBuyOrderBook okexBuyOrderBook;
    @Autowired
    OkexSellOrderBook okexSellOrderBook;

    public OkexOrderBook getService(String side) throws OkexFactoryException {

        if (side.toLowerCase().equals("buy")) return okexBuyOrderBook;
        if (side.toLowerCase().equals("sell")) return okexSellOrderBook;

        throw new OkexFactoryException("Implementação não encontrada. " +
                "Implementações atuais: sell e buy");

    }

}
