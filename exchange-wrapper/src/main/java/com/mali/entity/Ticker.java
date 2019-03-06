package com.mali.entity;

public class Ticker {

    private double currentPrice;
    private double last24Volume;

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getLast24Volume() {
        return last24Volume;
    }

    public void setLast24Volume(double last24Volume) {
        this.last24Volume = last24Volume;
    }
}
