package com.crypfy.elastic.trader.trade;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.*;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import com.crypfy.elastic.trader.trade.json.RequirementsResponse;

import java.util.List;

public interface TradeServices {

    public boolean exchangeHasMarket(Market market, ExchangerDetails exchanger) throws TradeException;
    public boolean exchangeHasCoin(Market market, ExchangerDetails exchanger) throws TradeException;
    public Market marketFromOrder(Order order);
    public double exchangersTotalBalance(List<ExchangerDetails> exchangers, BaseCurrency baseCurrency, double percentOfTotal) throws TradeException;
    public List<ExchangerDetails> exchangersWithMarketOrCoin(List<ExchangerDetails> exchangers,Market market) throws TradeException;
    public double currentUSDBRLRatio() throws TradeException;

    //trade related
    public boolean coinInTrade(String callCoin,String strategyName,String strategyDetailsName,Exchanger exchanger) throws TradeException;
    public double coinBalance(ExchangerDetails exchanger,String coinName) throws TradeException;
    public boolean hitTP(Order order,Market market,ExchangerDetails exchanger) throws TradeException;
    public boolean hitSL(Order order,Market market,ExchangerDetails exchanger) throws TradeException;
    public boolean closeConditionsMet(Order order,Market market,ExchangerDetails exchangerDetails) throws TradeException;
    public OrderCloseReason closeReason(Order order, Market market, ExchangerDetails exchanger) throws TradeException;
    public boolean minimumConditionsMet(Order order,SubStrategy subStrategy,ExchangerDetails exchangerDetails) throws TradeException;
}
