package com.mali.crypfy.clustering.simulations;

import java.util.Date;
import java.util.List;

public class MonthStats {

    private Date inicialDate;
    private Date endDate;
    private int nTransactions;
    private double returnTotal;
    private double returnTotalPercent;
    private double monthMaxDD;
    private List<Double> balanceUpdates;

    public Date getInicialDate() {
        return inicialDate;
    }

    public void setInicialDate(Date inicialDate) {
        this.inicialDate = inicialDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getnTransactions() {
        return nTransactions;
    }

    public void setnTransactions(int nTransactions) {
        this.nTransactions = nTransactions;
    }

    public double getReturnTotal() {
        return returnTotal;
    }

    public void setReturnTotal(double returnTotal) {
        this.returnTotal = returnTotal;
    }

    public double getReturnTotalPercent() {
        return returnTotalPercent;
    }

    public void setReturnTotalPercent(double returnTotalPercent) {
        this.returnTotalPercent = returnTotalPercent;
    }

    public double getMonthMaxDD() {
        return monthMaxDD;
    }

    public void setMonthMaxDD(double monthMaxDD) {
        this.monthMaxDD = monthMaxDD;
    }

    public List<Double> getBalanceUpdates() {
        return balanceUpdates;
    }

    public void setBalanceUpdates(List<Double> balanceUpdates) {
        this.balanceUpdates = balanceUpdates;
    }
}
