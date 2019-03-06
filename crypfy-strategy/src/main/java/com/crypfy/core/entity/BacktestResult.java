package com.crypfy.core.entity;

import com.crypfy.core.enumerations.StrategyPeriod;
import java.util.List;

public class BacktestResult {

    private String strategyName;
    private double periodMeanReturn;
    private double totalReturn;
    private StrategyPeriod period;
    private double monthlyMeanReturn;
    private double maxdd;
    private List<Double> periodReturns;
    private List<Double> periodReturnsPercent;
    private List<Double> volValues;
    private Integer nPeriods;

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public double getPeriodMeanReturn() {
        return periodMeanReturn;
    }

    public void setPeriodMeanReturn(double periodMeanReturn) {
        this.periodMeanReturn = periodMeanReturn;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }

    public StrategyPeriod getPeriod() {
        return period;
    }

    public void setPeriod(StrategyPeriod period) {
        this.period = period;
    }

    public double getMonthlyMeanReturn() {
        return monthlyMeanReturn;
    }

    public void setMonthlyMeanReturn(double monthlyMeanReturn) {
        this.monthlyMeanReturn = monthlyMeanReturn;
    }

    public double getMaxdd() {
        return maxdd;
    }

    public void setMaxdd(double maxdd) {
        this.maxdd = maxdd;
    }

    public List<Double> getPeriodReturns() {
        return periodReturns;
    }

    public void setPeriodReturns(List<Double> periodReturns) {
        this.periodReturns = periodReturns;
    }

    public Integer getnPeriods() {
        return nPeriods;
    }

    public void setnPeriods(Integer nPeriods) {
        this.nPeriods = nPeriods;
    }

    public List<Double> getPeriodReturnsPercent() {
        return periodReturnsPercent;
    }

    public void setPeriodReturnsPercent(List<Double> periodReturnsPercent) {
        this.periodReturnsPercent = periodReturnsPercent;
    }

    public List<Double> getVolValues() {
        return volValues;
    }

    public void setVolValues(List<Double> volValues) {
        this.volValues = volValues;
    }
}
