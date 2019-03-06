package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DealDetails {

    private String uuid;

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
