package com.mali.exchanger.communication.impl.foxbit.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDetails {

    private String clOrdID;
    private long orderID;
    private double cumQty;
    private String ordStatus;
    private double leavesQty;
    private double cxlQty;
    private double avgPx;
    private String symbol;
    private String side;
    private String ordType;
    private double orderQty;
    private double price;
    private String orderDate;
    private double volume;
    private String timeInForce;

    @JsonProperty(value = "ClOrdID")
    public String getClOrdID() {
        return clOrdID;
    }

    public void setClOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
    }

    @JsonProperty(value = "OrderID")
    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    @JsonProperty(value = "CumQty")
    public double getCumQty() {
        return cumQty;
    }

    public void setCumQty(double cumQty) {
        this.cumQty = cumQty;
    }

    @JsonProperty(value = "OrdStatus")
    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    @JsonProperty(value = "LeavesQty")
    public double getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
    }

    @JsonProperty(value = "CxlQty")
    public double getCxlQty() {
        return cxlQty;
    }

    public void setCxlQty(double cxlQty) {
        this.cxlQty = cxlQty;
    }

    @JsonProperty(value = "AvgPx")
    public double getAvgPx() {
        return avgPx;
    }

    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }

    @JsonProperty(value = "Symbol")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty(value = "Side")
    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @JsonProperty(value = "OrdType")
    public String getOrdType() {
        return ordType;
    }

    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }

    @JsonProperty(value = "OrderQty")
    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    @JsonProperty(value = "Price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty(value = "OrderDate")
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @JsonProperty(value = "Volume")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @JsonProperty(value = "TimeInForce")
    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }
}
