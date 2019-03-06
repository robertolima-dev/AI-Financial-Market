package com.mali.exchanger.communication.impl.binance.factories;

import com.mali.exchanger.communication.impl.binance.BinanceOrderBook;
import com.mali.exchanger.communication.impl.binance.exceptions.BinanceFactoryException;
import com.mali.exchanger.communication.impl.binance.implementations.orderBook.BinanceBuyOrderBookImpl;
import com.mali.exchanger.communication.impl.binance.implementations.orderBook.BinanceSellOrderBookImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinanceOrderBookFactory {

    @Autowired
    BinanceBuyOrderBookImpl binanceBuyOrderBook;
    @Autowired
    BinanceSellOrderBookImpl binanceSellOrderBook;

    public BinanceOrderBook getService(String side) throws BinanceFactoryException {

        if (side.toLowerCase().equals("buy")) return binanceBuyOrderBook;
        if (side.toLowerCase().equals("sell")) return binanceSellOrderBook;

        throw new BinanceFactoryException("Implementações válidas: buy e sell");
    }

}
