package com.mali.exchanger.communication.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class DealFactoryException extends Exception {

    private List<ServiceErrorItem> errors;

    public DealFactoryException(String message, List<ServiceErrorItem> errors) {
        super(message);
        this.errors = errors;
    }

    public DealFactoryException(String message) {
        super(message);
    }

    public List<ServiceErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceErrorItem> errors) {
        this.errors = errors;
    }
}
