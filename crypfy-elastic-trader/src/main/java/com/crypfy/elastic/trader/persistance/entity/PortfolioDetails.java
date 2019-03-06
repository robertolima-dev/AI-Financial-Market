package com.crypfy.elastic.trader.persistance.entity;

public class PortfolioDetails {

    private String coinName;
    private double percentage;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
