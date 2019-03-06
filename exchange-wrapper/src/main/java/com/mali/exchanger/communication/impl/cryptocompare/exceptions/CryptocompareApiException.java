package com.mali.exchanger.communication.impl.cryptocompare.exceptions;

import com.mali.validator.ServiceErrorItem;
import java.util.List;

public class CryptocompareApiException extends Exception{

    private List<ServiceErrorItem> errors;

    public CryptocompareApiException(String message) {
        super(message);
    }

    public CryptocompareApiException(String message, List<ServiceErrorItem> errors) {
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
