package com.mali.exchanger.communication.impl.binance;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.binance.exceptions.BinanceOrderBookException;

import java.util.List;

public interface BinanceOrderBook {
    public List<SimpleOrder> getOrderBook(Market market) throws BinanceOrderBookException;
}
