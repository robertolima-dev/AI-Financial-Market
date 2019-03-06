package com.mali.crypfy.clustering.integrations.mali.coin.data.exception;

import org.springframework.validation.FieldError;
import java.util.List;

public class CoinException extends Exception {

    private List<FieldError> errors;

    public CoinException(String msg) {
        super(msg);
    }

    public CoinException(String msg, List<FieldError> errors) {
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
