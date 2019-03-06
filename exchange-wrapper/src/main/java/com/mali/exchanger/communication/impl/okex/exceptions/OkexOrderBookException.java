package com.mali.exchanger.communication.impl.okex.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class OkexOrderBookException extends Exception {

    private List<ServiceErrorItem> errors;

    public OkexOrderBookException(String msg) {
        super(msg);
    }

    public OkexOrderBookException(String msg, List<ServiceErrorItem> errors) {
        super(msg);
        this.errors = errors;
    }

    public List<ServiceErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceErrorItem> errors) {
        this.errors = errors;
    }

}
