package com.crypfy.elastic.trader.persistance.entity;

import com.crypfy.elastic.trader.persistance.enums.Exchanger;

public class ExchangerDetails {

    private Exchanger exchanger;
    private String key;
    private String secret;

    public Exchanger getExchanger() {
        return exchanger;
    }

    public void setExchanger(Exchanger exchanger) {
        this.exchanger = exchanger;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
