package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerDetails {

    private double high;
    private double low;
    private double volume;
    private double volumeBTC;
    private double lastPrice;
    private double bid;
    private double ask;
    private int openBuyOrders;
    private int openSellOrder;
    private String timestamp;

    @JsonProperty("High")
    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    @JsonProperty("Low")
    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @JsonProperty("Volume")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @JsonProperty("BaseVolume")
    public double getVolumeBTC() {
        return volumeBTC;
    }

    public void setVolumeBTC(double volumeBTC) {
        this.volumeBTC = volumeBTC;
    }

    @JsonProperty("Last")
    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    @JsonProperty("Bid")
    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    @JsonProperty("Ask")
    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    @JsonProperty("OpenBuyOrders")
    public int getOpenBuyOrders() {
        return openBuyOrders;
    }

    public void setOpenBuyOrders(int openBuyOrders) {
        this.openBuyOrders = openBuyOrders;
    }

    @JsonProperty("OpenSellOrders")
    public int getOpenSellOrder() {
        return openSellOrder;
    }

    public void setOpenSellOrder(int openSellOrder) {
        this.openSellOrder = openSellOrder;
    }

    @JsonProperty("TimeStamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
