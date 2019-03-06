package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import java.util.List;

public class WithdrawHistory {
    private boolean success;
    private String message;
    private List<WithdrawHistoryDetails> result;

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

    public List<WithdrawHistoryDetails> getResult() {
        return result;
    }

    public void setResult(List<WithdrawHistoryDetails> result) {
        this.result = result;
    }
}
