package com.mali.config.okex;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.services.okex-api")
public class ConfigOkexApi {

    private String markets;
    private String marketHistory;
    private String orderBook;
    private String newOrder;
    private String balances;
    private String orderCancel;
    private String getOrder;
    private String withdraw;
    private String orderHistory;
    private String ticker;
    private String depositWithdrawHistory;
    private String transfer;

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

    public String getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(String newOrder) {
        this.newOrder = newOrder;
    }

    public String getBalances() {
        return balances;
    }

    public void setBalances(String balances) {
        this.balances = balances;
    }

    public String getOrderCancel() {
        return orderCancel;
    }

    public void setOrderCancel(String orderCancel) {
        this.orderCancel = orderCancel;
    }

    public String getGetOrder() {
        return getOrder;
    }

    public void setGetOrder(String getOrder) {
        this.getOrder = getOrder;
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

    public String getDepositWithdrawHistory() {
        return depositWithdrawHistory;
    }

    public void setDepositWithdrawHistory(String depositWithdrawHistory) {
        this.depositWithdrawHistory = depositWithdrawHistory;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }
}
