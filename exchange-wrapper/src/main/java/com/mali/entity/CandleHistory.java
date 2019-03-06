package com.mali.entity;

import java.util.List;

public class CandleHistory {

    private List<Candlestick> history;
    private Market market;

    public List<Candlestick> getHistory() {
        return history;
    }

    public void setHistory(List<Candlestick> history) {
        this.history = history;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
