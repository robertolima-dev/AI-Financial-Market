package com.crypfy.elastic.trader.strategy.exceptions;


import com.crypfy.elastic.trader.integrations.ServiceItemError;

import java.util.List;

public class StrategyStatusFactoryException extends Exception{
    private int status;
    private List<ServiceItemError> errors;

    public StrategyStatusFactoryException(String msg) {
        super(msg);
    }

    public StrategyStatusFactoryException(String msg, List<ServiceItemError> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public StrategyStatusFactoryException(String msg, int status) {
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
