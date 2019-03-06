package com.mali.exchanger.communication.impl.foxbit.sub.entity;

public class OrderBook {

    //the order of bids and aks is:
    //price
    //amount
    //time

    private String pair;
    private double[][] bids;
    private double[][] asks;

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
