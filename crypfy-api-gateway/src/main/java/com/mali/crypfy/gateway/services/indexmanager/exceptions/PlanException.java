package com.mali.crypfy.gateway.services.indexmanager.exceptions;

import com.mali.crypfy.gateway.services.ServiceItemError;

import java.util.List;

public class PlanException extends Exception{
    private int status;
    private List<ServiceItemError> errors;

    public PlanException(String msg) {
        super(msg);
    }

    public PlanException(String msg,List<ServiceItemError> errors,int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public PlanException(String msg,int status) {
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
