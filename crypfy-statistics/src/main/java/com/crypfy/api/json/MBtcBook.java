package com.crypfy.api.json;

public class MBtcBook {

    private double [][] bids;
    private double [][] asks;

    public double[][] getBids() {
        return bids;
    }

    public void setBids(double[][] bids) {
        this.bids = bids;
    }

    public double[][] getAsks() {
        return asks;
    }

    public void setAsks(double[][] asks) {
        this.asks = asks;
    }
}
