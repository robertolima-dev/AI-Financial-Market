package com.mali.exchanger.communication.impl.bittrex.factories;

import com.mali.exchanger.communication.impl.bittrex.BittrexOrderBook;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexOrderBookFactoryException;
import com.mali.exchanger.communication.impl.bittrex.implementations.orderbook.BittrexBuyOrderBookImpl;
import com.mali.exchanger.communication.impl.bittrex.implementations.orderbook.BittrexSellOrderBookImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BittrexOrderBookFactory {

    @Autowired
    BittrexBuyOrderBookImpl buyOrderBook;
    @Autowired
    BittrexSellOrderBookImpl sellOrderBook;

    public BittrexOrderBook getService(String side) throws BittrexOrderBookFactoryException {

        if (side.equals("buy")) return buyOrderBook;
        if (side.equals("sell")) return sellOrderBook;

        throw new BittrexOrderBookFactoryException("Implementação não encontrada. " +
                "Implementações possiveis: buy e sell");

    }

}
