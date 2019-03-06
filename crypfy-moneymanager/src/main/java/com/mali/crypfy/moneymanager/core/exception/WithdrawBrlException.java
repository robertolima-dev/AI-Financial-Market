package com.mali.crypfy.moneymanager.core.exception;

import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;

import java.util.List;

public class WithdrawBrlException extends Exception {

    private List<ServiceItemError> errors;
    private int status;

    public WithdrawBrlException(String message){
        super(message);
    }

    public WithdrawBrlException(String message, List<ServiceItemError> errors) {
        super(message);
        this.errors = errors;
    }

    public WithdrawBrlException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public WithdrawBrlException(String message, List<ServiceItemError> errors, int status) {
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
