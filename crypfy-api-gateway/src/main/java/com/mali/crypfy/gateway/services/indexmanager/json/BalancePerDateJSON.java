package com.mali.crypfy.gateway.services.indexmanager.json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BalancePerDateJSON implements Serializable {

    private BigDecimal balance;
    private Date date;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
