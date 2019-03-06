package com.mali.exchanger.communication.impl.bittrex.sub.entity;

public class DealStatus {
    private boolean success;
    private String message;
    private DealDetails result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DealDetails getResult() {
        return result;
    }

    public void setResult(DealDetails result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
