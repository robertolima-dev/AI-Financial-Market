package com.mali.crypfy.indexmanager.core.exception;

import java.util.List;
import java.util.Map;

public class PlanRequestProfitAddAllException extends Exception {

    private int status;
    private Map<String,List<ServiceItemError>> errors;

    public PlanRequestProfitAddAllException(String msg,int status,Map<String,List<ServiceItemError>> errors) {
        super(msg);
        this.status = status;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, List<ServiceItemError>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<ServiceItemError>> errors) {
        this.errors = errors;
    }
}
