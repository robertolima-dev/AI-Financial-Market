package com.mali.crypfy.moneymanager.core.exception;

import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;

import java.util.List;

public class BankException extends Exception {

    private List<ServiceItemError> errors;
    private int status;

    public BankException(String msg) {
        super(msg);
    }

    public BankException(String msg, List<ServiceItemError> errors) {
        super(msg);
        this.errors = errors;
    }

    public BankException(String msg, List<ServiceItemError> errors,int status) {
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
