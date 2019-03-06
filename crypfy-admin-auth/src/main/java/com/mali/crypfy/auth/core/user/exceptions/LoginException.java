package com.mali.crypfy.auth.core.user.exceptions;

import com.mali.crypfy.auth.validator.ServiceErrorItem;

import java.util.List;

public class LoginException extends Exception {

    private List<ServiceErrorItem> errors;
    private int status;

    public LoginException(String msg) {
        super(msg);
    }

    public LoginException(String msg,List<ServiceErrorItem> errors) {
        super(msg);
        this.errors = errors;
    }

    public LoginException(String msg,List<ServiceErrorItem> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public List<ServiceErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceErrorItem> errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
