package com.mali.exchanger.communication.impl.okex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeResult {

    private boolean result;
    private Long orderId;
    private Long errorCode;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @JsonProperty("order_id")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("error_code")
    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }
}
