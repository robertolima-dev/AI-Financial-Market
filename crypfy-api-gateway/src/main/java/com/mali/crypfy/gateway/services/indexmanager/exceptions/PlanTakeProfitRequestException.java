package com.mali.crypfy.gateway.services.indexmanager.exceptions;

import com.mali.crypfy.gateway.services.ServiceItemError;

import java.util.List;
import java.util.Map;

public class PlanTakeProfitRequestException extends Exception{
    private int status;
    private List<ServiceItemError> errors;
    private Map<String,List<ServiceItemError>> multiErrors;

    public PlanTakeProfitRequestException(String msg) {
        super(msg);
    }

    public PlanTakeProfitRequestException(String msg, List<ServiceItemError> errors, int status) {
        super(msg);
        this.errors = errors;
        this.status = status;
    }

    public PlanTakeProfitRequestException(String msg,Map<String,List<ServiceItemError>> multiErrors, int status) {
        super(msg);
        this.status = status;
        this.multiErrors = multiErrors;
    }

    public PlanTakeProfitRequestException(String msg, int status) {
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

    public Map<String, List<ServiceItemError>> getMultiErrors() {
        return multiErrors;
    }

    public void setMultiErrors(Map<String, List<ServiceItemError>> multiErrors) {
        this.multiErrors = multiErrors;
    }
}
