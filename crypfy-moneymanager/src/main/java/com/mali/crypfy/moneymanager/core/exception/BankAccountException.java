package com.mali.crypfy.moneymanager.core.exception;

import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;

import java.util.List;

/**
 * Bank Account Exception
 */
public class BankAccountException extends Exception {

    private List<ServiceItemError> errors;
    private int status;

    public BankAccountException(String msg) {
        super(msg);
    }

    public BankAccountException(String msg, List<ServiceItemError> errors) {
        super(msg);
        this.errors = errors;
    }

    public BankAccountException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public BankAccountException(String msg,List<ServiceItemError> errors,int status) {
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
