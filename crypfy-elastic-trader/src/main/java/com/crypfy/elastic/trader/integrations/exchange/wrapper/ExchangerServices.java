package com.crypfy.elastic.trader.integrations.exchange.wrapper;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.*;
import com.crypfy.elastic.trader.persistance.enums.BaseCurrency;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangerServices {

    public List<Market> getMarkets(Exchanger exchanger) throws ExchangerException;
    public BigDecimal getTotalBalance(Exchanger exchanger, BaseCurrency baseCurrency,String key,String secret) throws ExchangerException;
    public TradeResult newOrder(Exchanger exchanger, Market market, DealRequest dealRequest,String key,String secret) throws ExchangerException;
    public List<Balance> coinBalances(Exchanger exchanger,String key,String secret) throws ExchangerException;
    public BigDecimal getCoinBalance(Exchanger exchanger, String coin,String key,String secret) throws ExchangerException;
    public BigDecimal dealPrice(Exchanger exchanger, Market market,String type,double amount) throws ExchangerException;
    public Ticker getTicker(Exchanger exchanger, Market market) throws ExchangerException;
}
