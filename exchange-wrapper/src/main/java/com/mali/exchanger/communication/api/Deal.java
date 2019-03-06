package com.mali.exchanger.communication.api;

import com.mali.entity.Market;
import com.mali.entity.TradeResult;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.exceptions.DealException;

public interface Deal {

    public TradeResult newDeal(Exchanger exchanger,Market market, double quantity, double price, String key, String secret) throws DealException;
    public TradeResult checkDeal(Exchanger exchanger,TradeResult tradeResult, Market market, String key, String secret) throws DealException;
}
