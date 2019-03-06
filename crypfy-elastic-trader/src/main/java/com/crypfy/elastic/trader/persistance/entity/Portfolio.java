package com.crypfy.elastic.trader.persistance.entity;

import java.util.List;

public class Portfolio {

    private int month;
    private int year;
    private int day;
    private List<PortfolioDetails> coins;
    private double percentageChange;
    private String porfolioName;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<PortfolioDetails> getCoins() {
        return coins;
    }

    public void setCoins(List<PortfolioDetails> coins) {
        this.coins = coins;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public String getPorfolioName() {
        return porfolioName;
    }

    public void setPorfolioName(String porfolioName) {
        this.porfolioName = porfolioName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
