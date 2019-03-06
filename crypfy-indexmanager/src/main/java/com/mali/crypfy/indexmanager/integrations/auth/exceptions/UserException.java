package com.mali.crypfy.indexmanager.integrations.auth.exceptions;

import com.mali.crypfy.indexmanager.core.exception.ServiceItemError;

import java.util.List;

public class UserException extends Exception {
    private List<ServiceItemError> errors;
    private int status;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, List<ServiceItemError> errors) {
        super(message);
        this.errors = errors;
    }

    public UserException(String message, List<ServiceItemError> errors, int status) {
        super(message);
        this.errors = errors;
        this.status = status;
    }

    public List<ServiceItemError> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceItemError> errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
