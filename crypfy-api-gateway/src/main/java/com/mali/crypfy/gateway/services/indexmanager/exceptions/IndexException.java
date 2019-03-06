package com.mali.crypfy.gateway.services.indexmanager.exceptions;

public class IndexException extends Exception {
    private int status;

    public IndexException(String msg) {
        super(msg);
    }

    public IndexException(String msg,int status) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
