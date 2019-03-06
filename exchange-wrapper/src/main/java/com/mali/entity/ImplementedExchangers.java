package com.mali.entity;

import java.util.List;

public class ImplementedExchangers {

    private String msg;
    private List<String> exchangers;

    public ImplementedExchangers(List<String> exchangers, String msg) {
        this.exchangers = exchangers;
        this.msg = msg;
    }

    public List<String> getExchangers() {
        return exchangers;
    }

    public void setExchangers(List<String> exchangers) {
        this.exchangers = exchangers;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
