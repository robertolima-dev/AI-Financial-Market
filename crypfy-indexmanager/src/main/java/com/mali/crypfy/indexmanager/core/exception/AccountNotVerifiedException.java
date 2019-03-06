package com.mali.crypfy.indexmanager.core.exception;

import java.util.List;

public class AccountNotVerifiedException extends Exception {

    private int status;
    private List<ServiceItemError> errors;

    public AccountNotVerifiedException(String msg) {
        super(msg);
    }

    public AccountNotVerifiedException(String msg, List<ServiceItemError> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public AccountNotVerifiedException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ServiceItemError> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceItemError> errors) {
        this.errors = errors;
    }
}
