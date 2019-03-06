package com.crypfy.elastic.trader.intelligence.exceptions;

import com.crypfy.elastic.trader.integrations.ServiceItemError;

import java.util.List;

public class OrderSearcherException extends Exception{
    private int status;
    private List<ServiceItemError> errors;

    public OrderSearcherException(String msg) {
        super(msg);
    }

    public OrderSearcherException(String msg, List<ServiceItemError> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public OrderSearcherException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ServiceItemError> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceItemError> errors) {
        this.errors = errors;
    }
}
