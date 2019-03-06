package com.mali.exchanger.communication.impl.bittrex.sub.entity;

public class DepositData {

    private boolean success;
    private String message;
    private DepositDetails result;

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

    public DepositDetails getResult() {
        return result;
    }

    public void setResult(DepositDetails result) {
        this.result = result;
    }
}
