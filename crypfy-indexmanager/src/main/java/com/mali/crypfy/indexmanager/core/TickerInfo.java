package com.mali.crypfy.indexmanager.core;

import java.math.BigDecimal;

public class TickerInfo {

    private String label;
    private BigDecimal balance;
    private BigDecimal percent;
    private String labelPast;
    private BigDecimal balancePast;
    private BigDecimal percentPast;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public String getLabelPast() {
        return labelPast;
    }

    public void setLabelPast(String labelPast) {
        this.labelPast = labelPast;
    }

    public BigDecimal getBalancePast() {
        return balancePast;
    }

    public void setBalancePast(BigDecimal balancePast) {
        this.balancePast = balancePast;
    }

    public BigDecimal getPercentPast() {
        return percentPast;
    }

    public void setPercentPast(BigDecimal percentPast) {
        this.percentPast = percentPast;
    }
}
