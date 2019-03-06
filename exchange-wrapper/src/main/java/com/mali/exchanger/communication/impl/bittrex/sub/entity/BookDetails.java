package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookDetails {

    private double quantity;
    private double rate;

    @JsonProperty("Quantity")
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("Rate")
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
