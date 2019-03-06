package com.mali.exchanger.communication.impl.binance.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class BinanceOrderBookException extends  Exception {

    private List<ServiceErrorItem> errors;

    public BinanceOrderBookException(String message) {
        super(message);
    }

    public BinanceOrderBookException(String message, List<ServiceErrorItem> errors) {
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
