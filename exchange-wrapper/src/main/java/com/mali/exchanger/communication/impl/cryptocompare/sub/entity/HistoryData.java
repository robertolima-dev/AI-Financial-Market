package com.mali.exchanger.communication.impl.cryptocompare.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mali.entity.Candlestick;

import java.util.List;

public class HistoryData {
    private String response;
    private List<Candlestick> history;

    @JsonProperty("Response")
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @JsonProperty("Data")
    public List<Candlestick> getHistory() {
        return history;
    }

    public void setHistory(List<Candlestick> history) {
        this.history = history;
    }
}
