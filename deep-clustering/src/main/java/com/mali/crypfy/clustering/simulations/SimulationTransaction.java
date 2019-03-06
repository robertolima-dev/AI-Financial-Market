package com.mali.crypfy.clustering.simulations;

public class SimulationTransaction {

    private long exitDay;
    private double profitPercent;
    private double investedAmount;
    private String coinName;

    public long getExitDay() {
        return exitDay;
    }

    public void setExitDay(long exitDay) {
        this.exitDay = exitDay;
    }

    public double getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(double profitPercent) {
        this.profitPercent = profitPercent;
    }

    public double getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(double investedAmount) {
        this.investedAmount = investedAmount;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}
