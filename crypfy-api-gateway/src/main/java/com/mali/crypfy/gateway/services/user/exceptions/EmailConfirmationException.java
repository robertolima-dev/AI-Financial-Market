package com.mali.crypfy.gateway.services.user.exceptions;

import com.mali.crypfy.gateway.services.ServiceItemError;

import java.util.List;

public class EmailConfirmationException extends Exception {
    private List<ServiceItemError> errors;
    private int status;

    public EmailConfirmationException(String msg) {
        super(msg);
    }

    public EmailConfirmationException(String msg, List<ServiceItemError> errors) {
        super(msg);
        this.errors = errors;
    }

    public EmailConfirmationException(String msg, List<ServiceItemError> errors, int status) {
        super(msg);
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
