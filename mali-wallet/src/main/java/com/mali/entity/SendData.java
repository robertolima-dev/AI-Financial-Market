package com.mali.entity;

import java.math.BigDecimal;

public class SendData {
    private String address;
    private BigDecimal value;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
