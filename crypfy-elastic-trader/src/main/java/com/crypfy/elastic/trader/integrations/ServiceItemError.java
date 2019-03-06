package com.crypfy.elastic.trader.integrations;

public class ServiceItemError {

    public ServiceItemError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
