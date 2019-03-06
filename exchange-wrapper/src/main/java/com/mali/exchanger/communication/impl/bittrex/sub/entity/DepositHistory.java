package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import java.util.List;

public class DepositHistory {

    private boolean success;
    private String message;
    private List<DepositHistoryDetails> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DepositHistoryDetails> getResult() {
        return result;
    }

    public void setResult(List<DepositHistoryDetails> result) {
        this.result = result;
    }
}
