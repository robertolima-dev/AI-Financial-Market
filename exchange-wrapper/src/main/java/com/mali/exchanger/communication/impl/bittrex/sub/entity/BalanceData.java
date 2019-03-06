package com.mali.exchanger.communication.impl.bittrex.sub.entity;

public class BalanceData {
    private boolean success;
    private String message;
    private BalanceDetails result;

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

    public BalanceDetails getResult() {
        return result;
    }

    public void setResult(BalanceDetails result) {
        this.result = result;
    }
}
