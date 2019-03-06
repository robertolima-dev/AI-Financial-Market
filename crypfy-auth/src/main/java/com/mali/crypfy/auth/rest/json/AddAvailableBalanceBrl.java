package com.mali.crypfy.auth.rest.json;

import java.io.Serializable;
import java.math.BigDecimal;

public class AddAvailableBalanceBrl implements Serializable {
    private String email;
    private BigDecimal amount;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
