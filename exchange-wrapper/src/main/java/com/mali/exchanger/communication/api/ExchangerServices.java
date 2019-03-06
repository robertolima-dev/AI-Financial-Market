package com.mali.exchanger.communication.api;

import com.mali.entity.*;
import com.mali.enumerations.DealType;
import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import java.util.List;

public interface ExchangerServices {

    public String getDepositAddress(String coin,String key,String secret) throws ExchangerServicesException;
    public boolean doWithdraw(String password,String address,String coin, double quantity,String key,String secret) throws ExchangerServicesException;

    public double getCurrentTotalBalance(String key,String secret,String baseCurrency) throws ExchangerServicesException;
    public double getCoinBalance(String coin,String key,String secret) throws ExchangerServicesException;
    public List<Balance> getAllCoinBalances(String key, String secret) throws ExchangerServicesException;

    public List<Order> getOrderHistory(String key, String secret) throws ExchangerServicesException;
    public List<Deposit> getDepositHistory(String coin,String key, String secret) throws ExchangerServicesException;
    public List<Withdraw> getWithdrawHistory(String coin,String key, String secret) throws ExchangerServicesException;
    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, TimeFrame timeFrame) throws ExchangerServicesException;
    public Order orderInfo(Market market,String uuid, String key, String secret) throws ExchangerServicesException;

    public List<Market> getMarkets() throws ExchangerServicesException;
    public List<SimpleOrder> getOrderBook(Market market,String side) throws ExchangerServicesException;
    public Ticker getTicker(Market market) throws ExchangerServicesException;
    public TradeResult newDeal(Market market, DealType dealType, double quantity,double price, String key, String secret) throws ExchangerServicesException;
    public double dealPrice(Market market, DealType dealType,double coinAmount) throws ExchangerServicesException;
    public boolean orderCancel(String orderId,Market market,String key,String secret) throws ExchangerServicesException;

}
