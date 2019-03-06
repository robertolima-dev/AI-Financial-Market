package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDetails {

    private String uuid;
    private String market;
    private double quantity;
    private double remaining;
    private double price;
    private String openTime;
    private String closeTime;
    private String orderType;
    private double cost;

    @JsonProperty("OrderUuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @JsonProperty("Exchange")
    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
    @JsonProperty("Quantity")
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    @JsonProperty("QuantityRemaining")
    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }
    @JsonProperty("Limit")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("Type")
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @JsonProperty("Opened")
    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("Closed")
    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    @JsonProperty("Price")
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
