package com.mali.crypfy.clustering.cluster.exceptions;


import org.springframework.validation.FieldError;
import java.util.List;

public class ComparativeMetricsException extends  Exception {

    private List<FieldError> errors;

    public ComparativeMetricsException(String msg) {
        super(msg);
    }

    public ComparativeMetricsException(String msg,List<FieldError> errors) {
        super(msg);
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

}
