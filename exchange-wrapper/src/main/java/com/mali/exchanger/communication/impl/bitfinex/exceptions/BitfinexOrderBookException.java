package com.mali.exchanger.communication.impl.bitfinex.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class BitfinexOrderBookException extends  Exception {

    private List<ServiceErrorItem> errors;

    public BitfinexOrderBookException(String message) {
        super(message);
    }

    public BitfinexOrderBookException(String message, List<ServiceErrorItem> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ServiceErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceErrorItem> errors) {
        this.errors = errors;
    }
}