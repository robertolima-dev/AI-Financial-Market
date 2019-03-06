package com.crypfy.elastic.trader.persistance.entity;

import com.crypfy.elastic.trader.persistance.enums.OrderPriority;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import com.crypfy.elastic.trader.persistance.enums.TimeFrame;

import java.util.Date;

public class SubStrategy {

    private int allowedSimultaneousTransaction;
    private int coinsToSearch;
    private int patternSize;
    private int minimumSimultaneousOpp;
    private boolean usingTPSL;
    private TimeFrame timeFrame;
    private OrderPriority priority;
    private IntelligenceType intelligenceType;
    private double balanceWeight;
    private Date lastSearchUpdate;
    private String name;

    public int getAllowedSimultaneousTransaction() {
        return allowedSimultaneousTransaction;
    }

    public void setAllowedSimultaneousTransaction(int allowedSimultaneousTransaction) {
        this.allowedSimultaneousTransaction = allowedSimultaneousTransaction;
    }

    public int getCoinsToSearch() {
        return coinsToSearch;
    }

    public void setCoinsToSearch(int coinsToSearch) {
        this.coinsToSearch = coinsToSearch;
    }

    public int getPatternSize() {
        return patternSize;
    }

    public void setPatternSize(int patternSize) {
        this.patternSize = patternSize;
    }

    public int getMinimumSimultaneousOpp() {
        return minimumSimultaneousOpp;
    }

    public void setMinimumSimultaneousOpp(int minimumSimultaneousOpp) {
        this.minimumSimultaneousOpp = minimumSimultaneousOpp;
    }

    public boolean isUsingTPSL() {
        return usingTPSL;
    }

    public void setUsingTPSL(boolean usingTPSL) {
        this.usingTPSL = usingTPSL;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

    public OrderPriority getPriority() {
        return priority;
    }

    public void setPriority(OrderPriority priority) {
        this.priority = priority;
    }

    public IntelligenceType getIntelligenceType() {
        return intelligenceType;
    }

    public void setIntelligenceType(IntelligenceType intelligenceType) {
        this.intelligenceType = intelligenceType;
    }

    public double getBalanceWeight() {
        return balanceWeight;
    }

    public void setBalanceWeight(double balanceWeight) {
        this.balanceWeight = balanceWeight;
    }

    public Date getLastSearchUpdate() {
        return lastSearchUpdate;
    }

    public void setLastSearchUpdate(Date lastSearchUpdate) {
        this.lastSearchUpdate = lastSearchUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
