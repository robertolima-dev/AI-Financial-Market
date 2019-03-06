package com.mali.exchanger.communication.impl.foxbit.exceptions;

import com.mali.validator.ServiceErrorItem;
import java.util.List;

public class FoxbitApiException extends Exception {

    private List<ServiceErrorItem> errors;

    public FoxbitApiException(String message) {
        super(message);
    }

    public FoxbitApiException(String message, List<ServiceErrorItem> errors) {
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
