package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import java.util.List;

public class Balances {
    private boolean success;
    private String message;
    private List<BalanceDetails> result;

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

    public List<BalanceDetails> getResult() {
        return result;
    }

    public void setResult(List<BalanceDetails> result) {
        this.result = result;
    }
}
