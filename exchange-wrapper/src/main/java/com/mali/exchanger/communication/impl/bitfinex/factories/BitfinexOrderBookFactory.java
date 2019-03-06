package com.mali.exchanger.communication.impl.bitfinex.factories;

import com.mali.exchanger.communication.impl.bitfinex.BitfinexOrderBook;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexOrderBookFactoryException;
import com.mali.exchanger.communication.impl.bitfinex.implementations.orderbook.BitfinexBuyOrderBookImpl;
import com.mali.exchanger.communication.impl.bitfinex.implementations.orderbook.BitfinexSellOrderBookImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitfinexOrderBookFactory {

    @Autowired
    BitfinexBuyOrderBookImpl buyOrderBook;
    @Autowired
    BitfinexSellOrderBookImpl sellOrderBook;

    public BitfinexOrderBook getService(String side) throws BitfinexOrderBookFactoryException {

        if (side.equals("buy")) return buyOrderBook;
        if (side.equals("sell")) return sellOrderBook;

        throw new BitfinexOrderBookFactoryException("Implementação não encontrada. " +
                "Implementação atuais: buy e sell");

    }

}
