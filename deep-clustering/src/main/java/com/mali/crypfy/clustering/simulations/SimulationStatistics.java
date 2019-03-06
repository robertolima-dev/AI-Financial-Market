package com.mali.crypfy.clustering.simulations;

import java.util.List;

public class SimulationStatistics {

    private List<Double> balanceUpdates;
    private double finalBalance;
    private double maxDD;
    private Integer nTransactions;
    private double returnPercent;
    private List<MonthStats> monthStats;
    private List<String> negociatedCoins;

    public List<Double> getBalanceUpdates() {
        return balanceUpdates;
    }

    public void setBalanceUpdates(List<Double> balanceUpdates) {
        this.balanceUpdates = balanceUpdates;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    public double getMaxDD() {
        return maxDD;
    }

    public void setMaxDD(double maxDD) {
        this.maxDD = maxDD;
    }

    public Integer getnTransactions() {
        return nTransactions;
    }

    public void setnTransactions(Integer nTransactions) {
        this.nTransactions = nTransactions;
    }

    public double getReturnPercent() {
        return returnPercent;
    }

    public void setReturnPercent(double returnPercent) {
        this.returnPercent = returnPercent;
    }

    public List<String> getNegociatedCoins() {
        return negociatedCoins;
    }

    public void setNegociatedCoins(List<String> negociatedCoins) {
        this.negociatedCoins = negociatedCoins;
    }

    public List<MonthStats> getMonthStats() {
        return monthStats;
    }

    public void setMonthStats(List<MonthStats> monthStats) {
        this.monthStats = monthStats;
    }
}
