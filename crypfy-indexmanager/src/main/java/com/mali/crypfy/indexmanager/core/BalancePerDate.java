package com.mali.crypfy.indexmanager.core;

import java.math.BigDecimal;
import java.util.Date;

public class BalancePerDate {

    public BalancePerDate(Date date,BigDecimal balance) {
        this.date = date;
        this.balance = balance;
    }

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
