package com.mali.exchanger.communication.impl.foxbit.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceDetails {

    private double btcLocked;
    private double brlLocked;
    private double btc;
    private double brl;

    @JsonProperty( value = "BTC_locked")
    public double getBtcLocked() {
        return btcLocked;
    }

    public void setBtcLocked(double btcLocked) {
        this.btcLocked = btcLocked;
    }

    @JsonProperty( value = "BRL_locked")
    public double getBrlLocked() {
        return brlLocked;
    }

    public void setBrlLocked(double brlLocked) {
        this.brlLocked = brlLocked;
    }

    @JsonProperty( value = "BTC")
    public double getBtc() {
        return btc;
    }

    public void setBtc(double btc) {
        this.btc = btc;
    }

    @JsonProperty( value = "BRL")
    public double getBrl() {
        return brl;
    }

    public void setBrl(double brl) {
        this.brl = brl;
    }
}
