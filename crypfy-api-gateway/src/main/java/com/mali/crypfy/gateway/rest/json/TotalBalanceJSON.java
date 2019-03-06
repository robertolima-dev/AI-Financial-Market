package com.mali.crypfy.gateway.rest.json;

import java.math.BigDecimal;

public class TotalBalanceJSON {

    private BigDecimal availableBalance;
    private BigDecimal blockedBalance;
    private BigDecimal bitcoinBalance;
    private BigDecimal estimatedTotalBalance;
    private boolean isPendingAccount;

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getBlockedBalance() {
        return blockedBalance;
    }

    public void setBlockedBalance(BigDecimal blockedBalance) {
        this.blockedBalance = blockedBalance;
    }

    public BigDecimal getBitcoinBalance() {
        return bitcoinBalance;
    }

    public void setBitcoinBalance(BigDecimal bitcoinBalance) {
        this.bitcoinBalance = bitcoinBalance;
    }

    public BigDecimal getEstimatedTotalBalance() {
        return estimatedTotalBalance;
    }

    public void setEstimatedTotalBalance(BigDecimal estimatedTotalBalance) {
        this.estimatedTotalBalance = estimatedTotalBalance;
    }

    public boolean isPendingAccount() {
        return isPendingAccount;
    }

    public void setPendingAccount(boolean pendingAccount) {
        isPendingAccount = pendingAccount;
    }
}
