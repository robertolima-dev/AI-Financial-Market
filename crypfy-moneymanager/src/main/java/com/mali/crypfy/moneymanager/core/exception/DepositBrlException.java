package com.mali.crypfy.moneymanager.core.exception;

import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;

import java.util.List;

public class DepositBrlException extends Exception {

    private List<ServiceItemError> errors;
    private int status;

    public DepositBrlException(String message){
        super(message);
    }

    public DepositBrlException(String message, List<ServiceItemError> errors) {
        super(message);
        this.errors = errors;
    }

    public DepositBrlException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public DepositBrlException(String message, List<ServiceItemError> errors, int status) {
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
