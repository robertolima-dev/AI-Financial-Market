package com.mali.crypfy.indexmanager.core.exception;

import java.util.List;

public class PlanRequestProfitException extends Exception {

    private List<ServiceItemError> errors;
    private int status;

    public PlanRequestProfitException(String msg) {
        super(msg);
    }

    public PlanRequestProfitException(String msg,List<ServiceItemError> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public PlanRequestProfitException(String msg,List<ServiceItemError> errors) {
        super(msg);
        this.errors = errors;
    }

    public PlanRequestProfitException(String msg,int status) {
        super(msg);
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
