package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceDetails {
    private String coinName;
    private double balance;
    private double pending;
    private String address;

    @JsonProperty("Currency")
    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
    @JsonProperty("Balance")
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    @JsonProperty("Pending")
    public double getPending() {
        return pending;
    }

    public void setPending(double pending) {
        this.pending = pending;
    }
    @JsonProperty("CryptoAddress")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
