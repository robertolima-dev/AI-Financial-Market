package com.mali.exchanger.communication.impl.foxbit.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalancesDetails {

    private BalanceDetails details;

    @JsonProperty( value = "4")
    public BalanceDetails getDetails() {
        return details;
    }

    public void setDetails(BalanceDetails details) {
        this.details = details;
    }
}
