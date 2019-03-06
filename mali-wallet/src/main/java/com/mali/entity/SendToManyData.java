package com.mali.entity;

import java.math.BigDecimal;
import java.util.Map;

public class SendToManyData {
    private String account;
    private Map<String,BigDecimal> adressValueMap;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Map<String, BigDecimal> getAdressValueMap() {
        return adressValueMap;
    }

    public void setAdressValueMap(Map<String, BigDecimal> adressValueMap) {
        this.adressValueMap = adressValueMap;
    }
}
