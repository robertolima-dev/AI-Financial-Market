package com.mali.exchanger.communication.impl.foxbit.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Order {

    private int status;
    private String description;
    private List<OrderDetails> responses;

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
    public List<OrderDetails> getResponses() {
        return responses;
    }

    public void setResponses(List<OrderDetails> responses) {
        this.responses = responses;
    }



}
