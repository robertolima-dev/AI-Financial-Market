package com.mali.exchanger.communication.impl.okex.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class OkexFactoryException extends Exception {

    private List<ServiceErrorItem> errors;

    public OkexFactoryException(String msg) {
        super(msg);
    }

    public OkexFactoryException(String msg, List<ServiceErrorItem> errors) {
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
