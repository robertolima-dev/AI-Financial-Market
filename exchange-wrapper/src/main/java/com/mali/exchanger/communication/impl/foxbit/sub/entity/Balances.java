package com.mali.exchanger.communication.impl.foxbit.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Balances {

    private int status;
    private String description;
    private List<BalancesDetails> responses;

    @JsonProperty(value = "Status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JsonProperty(value = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty(value = "Responses")
    public List<BalancesDetails> getResponses() {
        return responses;
    }

    public void setResponses(List<BalancesDetails> responses) {
        this.responses = responses;
    }
}
