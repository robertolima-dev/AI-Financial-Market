package com.crypfy.elastic.trader.integrations.mali.coin.data.exception;


import com.crypfy.elastic.trader.integrations.ServiceItemError;

import java.util.List;

public class CoinException extends Exception{
    private int status;
    private List<ServiceItemError> errors;

    public CoinException(String msg) {
        super(msg);
    }

    public CoinException(String msg, List<ServiceItemError> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public CoinException(String msg, int status) {
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
