package com.mali.exchanger.communication.impl.okex.sub.entity;

public class OrderBooks {

    private double[][] asks;
    private double[][] bids;

    public double[][] getAsks() {
        return asks;
    }

    public void setAsks(double[][] asks) {
        this.asks = asks;
    }

    public double[][] getBids() {
        return bids;
    }

    public void setBids(double[][] bids) {
        this.bids = bids;
    }
}
