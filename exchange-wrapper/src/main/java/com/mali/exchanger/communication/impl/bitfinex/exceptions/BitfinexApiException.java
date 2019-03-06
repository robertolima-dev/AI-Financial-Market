package com.mali.exchanger.communication.impl.bitfinex.exceptions;

import com.mali.validator.ServiceErrorItem;
import java.util.List;

public class BitfinexApiException extends  Exception {

    private List<ServiceErrorItem> errors;

    public BitfinexApiException(String message) {
        super(message);
    }

    public BitfinexApiException(String message, List<ServiceErrorItem> errors) {
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
