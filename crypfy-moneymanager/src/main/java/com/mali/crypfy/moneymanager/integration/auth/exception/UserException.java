package com.mali.crypfy.moneymanager.integration.auth.exception;

import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;

import java.util.List;

/**
 * User Service Exception
 */
public class UserException extends Exception {
    private List<ServiceItemError> errors;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, List<ServiceItemError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ServiceItemError> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceItemError> errors) {
        this.errors = errors;
    }
}
