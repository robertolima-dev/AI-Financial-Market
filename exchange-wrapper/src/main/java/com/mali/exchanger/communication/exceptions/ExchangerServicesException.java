package com.mali.exchanger.communication.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class ExchangerServicesException extends Exception {

    private List<ServiceErrorItem> errors;

    public ExchangerServicesException(String message, List<ServiceErrorItem> errors) {
        super(message);
        this.errors = errors;
    }

    public ExchangerServicesException(String message) {
        super(message);
    }

    public List<ServiceErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceErrorItem> errors) {
        this.errors = errors;
    }
}
