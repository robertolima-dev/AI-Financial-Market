package com.mali.exchanger.communication.impl.bitfinex.sub.entity;

import java.util.List;

public class OrderBook {
    private List<OrderBookDetails> bids;
    private List<OrderBookDetails> asks;

    public List<OrderBookDetails> getBids() {
        return bids;
    }

    public void setBids(List<OrderBookDetails> bids) {
        this.bids = bids;
    }

    public List<OrderBookDetails> getAsks() {
        return asks;
    }

    public void setAsks(List<OrderBookDetails> asks) {
        this.asks = asks;
    }
}
