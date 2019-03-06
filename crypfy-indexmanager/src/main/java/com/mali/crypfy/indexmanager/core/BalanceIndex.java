package com.mali.crypfy.indexmanager.core;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceIndex {

    public BalanceIndex(BigDecimal balance, Date date) {
        this.balance = balance;
        this.date = date;
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

    public String getDateStr() {
        return date.toString();
    }
}
