package com.mali.config.bitfinex;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.services.bitfinex-api")
public class ConfigBitfinexApi {

    private String markets;
    private String marketHistory;
    private String orderBook;
    private String newOrder;
    private String orderInfo;
    private String balances;
    private String orderCancel;
    private String openedOrders;
    private String depositAddress;
    private String withdraw;
    private String orderHistory;
    private String ticker;
    private String movementsHistory;
    private String baseUrl;

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

    public String getMovementsHistory() {
        return movementsHistory;
    }

    public void setMovementsHistory(String movementsHistory) {
        this.movementsHistory = movementsHistory;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
