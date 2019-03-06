package com.crypfy.elastic.trader.integrations.exchange.wrapper.json;

public class Balance {
    private String coin;
    private double balance;
    private double pending;
    private String address;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPending() {
        return pending;
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
