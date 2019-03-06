package com.mali.exchanger.communication.impl.bittrex.sub.entity;

public class WithdrawData {
    private boolean success;
    private String message;
    private WithdrawDetail result;

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

    public WithdrawDetail getResult() {
        return result;
    }

    public void setResult(WithdrawDetail result) {
        this.result = result;
    }
}
