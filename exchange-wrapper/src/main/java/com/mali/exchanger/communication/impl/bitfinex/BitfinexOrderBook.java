package com.mali.exchanger.communication.impl.bitfinex;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexOrderBookException;

import java.util.List;

public interface BitfinexOrderBook {

    public List<SimpleOrder> getOrderBook(Market market) throws BitfinexOrderBookException;

}
