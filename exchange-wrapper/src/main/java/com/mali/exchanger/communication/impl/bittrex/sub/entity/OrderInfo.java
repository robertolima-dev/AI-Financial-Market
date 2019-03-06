package com.mali.exchanger.communication.impl.bittrex.sub.entity;

public class OrderInfo {

    private boolean success;
    private int status;
    private OrderDetails result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderDetails getResult() {
        return result;
    }

    public void setResult(OrderDetails result) {
        this.result = result;
    }
}
