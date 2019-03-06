package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import java.util.List;

public class TickerData {
    private boolean success;
    private String message;
    private List<TickerDetails> result;

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

    public List<TickerDetails> getResult() {
        return result;
    }

    public void setResult(List<TickerDetails> result) {
        this.result = result;
    }
}
