package com.mali.exchanger.communication.impl.bitfinex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatus {
    private boolean isCancelled;

    @JsonProperty("is_cancelled")
    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
