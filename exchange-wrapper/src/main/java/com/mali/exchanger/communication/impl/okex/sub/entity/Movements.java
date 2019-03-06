package com.mali.exchanger.communication.impl.okex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Movements {

    private boolean result;
    private String symbol;
    private long errorCode;
    private List<MovementsDetail> records;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("error_code")
    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public List<MovementsDetail> getRecords() {
        return records;
    }

    public void setRecords(List<MovementsDetail> records) {
        this.records = records;
    }
}
