package com.mali.exchanger.communication.impl.okex;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexOrderBookException;

import java.util.List;

public interface OkexOrderBook {

    public List<SimpleOrder> getOrderBook(Market market) throws OkexOrderBookException;

}
