package com.mali.exchanger.communication.impl.okex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerRes {

    private long date;
    private Ticker ticker;
    private long errorCode;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    @JsonProperty("error_code")
    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }
}
