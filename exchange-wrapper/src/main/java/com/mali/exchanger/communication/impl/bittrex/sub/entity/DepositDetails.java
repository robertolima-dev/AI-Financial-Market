package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepositDetails {

    private String address;
    private String currency;

    @JsonProperty("Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("Currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
