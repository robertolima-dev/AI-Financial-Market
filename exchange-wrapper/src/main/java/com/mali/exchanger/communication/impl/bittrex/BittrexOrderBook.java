package com.mali.exchanger.communication.impl.bittrex;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexOrderBookException;

import java.util.List;

public interface BittrexOrderBook {

    public List<SimpleOrder> getBook(Market market) throws BittrexOrderBookException;

}
