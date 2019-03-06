package com.crypfy.api.json;

import java.util.List;

public class OrderBook {

    private boolean success;
    private String message;
    private List<BookDetails> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BookDetails> getResult() {
        return result;
    }

    public void setResult(List<BookDetails> result) {
        this.result = result;
    }
}
