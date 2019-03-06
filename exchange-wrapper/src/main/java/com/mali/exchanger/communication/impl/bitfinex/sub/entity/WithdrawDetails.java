package com.mali.exchanger.communication.impl.bitfinex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawDetails {

    private String status;
    private String message;
    private int withdrawalId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("withdrawal_id")
    public int getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(int withdrawalId) {
        this.withdrawalId = withdrawalId;
    }
}
