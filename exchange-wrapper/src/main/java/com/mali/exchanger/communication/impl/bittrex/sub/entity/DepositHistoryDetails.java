package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepositHistoryDetails {
    private double amount;
    private String coin;
    private int confirmations;
    private String timestamp;
    private String txId;
    private double txCost;
    private String address;
    private String uuid;

    @JsonProperty("Amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonProperty("Currency")
    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    @JsonProperty("Confirmations")
    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    @JsonProperty("LastUpdate")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("TxId")
    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    @JsonProperty("CryptoAddress")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("TxCost")
    public double getTxCost() {
        return txCost;
    }

    public void setTxCost(double txCost) {
        this.txCost = txCost;
    }

    @JsonProperty("PaymentUuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
