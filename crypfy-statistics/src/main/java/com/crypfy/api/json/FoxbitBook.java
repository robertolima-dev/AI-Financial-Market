package com.crypfy.api.json;

public class FoxbitBook {

    private String pair;
    private double [][] bids;
    private double [][] asks;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

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
