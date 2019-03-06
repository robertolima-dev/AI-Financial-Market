package com.mali.exchanger.communication.impl.bittrex.exceptions;

import com.mali.validator.ServiceErrorItem;
import java.util.List;

public class BittrexApiException extends Exception {

    private List<ServiceErrorItem> errors;

    public BittrexApiException(String msg) {
        super(msg);
    }

    public BittrexApiException(String msg,List<ServiceErrorItem> errors) {
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
