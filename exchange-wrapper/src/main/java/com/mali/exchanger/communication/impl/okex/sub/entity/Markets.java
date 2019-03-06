package com.mali.exchanger.communication.impl.okex.sub.entity;

import java.util.List;

public class Markets {

    private List<MarketString> data;

    public List<MarketString> getData() {
        return data;
    }

    public void setData(List<MarketString> data) {
        this.data = data;
    }
}
