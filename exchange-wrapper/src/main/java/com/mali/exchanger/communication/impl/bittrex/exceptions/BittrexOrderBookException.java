package com.mali.exchanger.communication.impl.bittrex.exceptions;

import com.mali.validator.ServiceErrorItem;

import java.util.List;

public class BittrexOrderBookException extends Exception {

    private List<ServiceErrorItem> errors;

    public BittrexOrderBookException(String msg) {
        super(msg);
    }

    public BittrexOrderBookException(String msg, List<ServiceErrorItem> errors) {
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
