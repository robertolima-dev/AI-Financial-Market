package com.mali.config.bittrex;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.services.bittrex-api")
public class ConfigBittrexApi {

    private String markets;
    private String marketHistory;
    private String orderBook;
    private String sell;
    private String buy;
    private String balances;
    private String balance;
    private String orderCancel;
    private String openedOrders;
    private String depositAddress;
    private String withdraw;
    private String orderHistory;
    private String ticker;
    private String withdrawHistory;
    private String depositHistory;
    private String getOrder;

    public String getMarkets() {
        return markets;
    }

    public void setMarkets(String markets) {
        this.markets = markets;
    }

    public String getMarketHistory() {
        return marketHistory;
    }

    public void setMarketHistory(String marketHistory) {
        this.marketHistory = marketHistory;
    }

    public String getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(String orderBook) {
        this.orderBook = orderBook;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getBalances() {
        return balances;
    }

    public void setBalances(String balances) {
        this.balances = balances;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOrderCancel() {
        return orderCancel;
    }

    public void setOrderCancel(String orderCancel) {
        this.orderCancel = orderCancel;
    }

    public String getOpenedOrders() {
        return openedOrders;
    }

    public void setOpenedOrders(String openedOrders) {
        this.openedOrders = openedOrders;
    }

    public String getDepositAddress() {
        return depositAddress;
    }

    public void setDepositAddress(String depositAddress) {
        this.depositAddress = depositAddress;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public String getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(String orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getWithdrawHistory() {
        return withdrawHistory;
    }

    public void setWithdrawHistory(String withdrawHistory) {
        this.withdrawHistory = withdrawHistory;
    }

    public String getDepositHistory() {
        return depositHistory;
    }

    public void setDepositHistory(String depositHistory) {
        this.depositHistory = depositHistory;
    }

    public String getGetOrder() {
        return getOrder;
    }

    public void setGetOrder(String getOrder) {
        this.getOrder = getOrder;
    }
}
