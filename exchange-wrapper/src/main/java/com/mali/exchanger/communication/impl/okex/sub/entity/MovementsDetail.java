package com.mali.exchanger.communication.impl.okex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovementsDetail {

    private String addr;
    private String account;
    private double amount;
    private String bank;
    private String benificiaryAddr;
    private double transactionValue;
    private double fee;
    private long date;
    private int status;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @JsonProperty("benificiary_addr")
    public String getBenificiaryAddr() {
        return benificiaryAddr;
    }

    public void setBenificiaryAddr(String benificiaryAddr) {
        this.benificiaryAddr = benificiaryAddr;
    }

    @JsonProperty("transaction_value")
    public double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
