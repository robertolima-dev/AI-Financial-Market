package com.mali.crypfy.gateway.rest.json;

import com.mali.crypfy.gateway.services.indexmanager.json.MonthlyPerfomanceJSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PlanPerfomanceDataJSON implements Serializable{

    private Integer idplan;
    private BigDecimal lastSixMonthPerfomance;
    private BigDecimal lastThreeMonthPerfomance;
    private BigDecimal currentMonthPerfomance;
    private BigDecimal lastMonthPerfomance;
    private BigDecimal yearToDatePerfomance;
    private List<MonthlyPerfomanceJSON> monthlyPerfomance;
    private BigDecimal userAvailableBalance;

    public BigDecimal getLastThreeMonthPerfomance() {
        return lastThreeMonthPerfomance;
    }

    public void setLastThreeMonthPerfomance(BigDecimal lastThreeMonthPerfomance) {
        this.lastThreeMonthPerfomance = lastThreeMonthPerfomance;
    }

    public BigDecimal getCurrentMonthPerfomance() {
        return currentMonthPerfomance;
    }

    public void setCurrentMonthPerfomance(BigDecimal currentMonthPerfomance) {
        this.currentMonthPerfomance = currentMonthPerfomance;
    }

    public BigDecimal getLastMonthPerfomance() {
        return lastMonthPerfomance;
    }

    public void setLastMonthPerfomance(BigDecimal lastMonthPerfomance) {
        this.lastMonthPerfomance = lastMonthPerfomance;
    }

    public List<MonthlyPerfomanceJSON> getMonthlyPerfomance() {
        return monthlyPerfomance;
    }

    public void setMonthlyPerfomance(List<MonthlyPerfomanceJSON> monthlyPerfomance) {
        this.monthlyPerfomance = monthlyPerfomance;
    }

    public BigDecimal getUserAvailableBalance() {
        return userAvailableBalance;
    }

    public void setUserAvailableBalance(BigDecimal userAvailableBalance) {
        this.userAvailableBalance = userAvailableBalance;
    }

    public BigDecimal getLastSixMonthPerfomance() {
        return lastSixMonthPerfomance;
    }

    public void setLastSixMonthPerfomance(BigDecimal lastSixMonthPerfomance) {
        this.lastSixMonthPerfomance = lastSixMonthPerfomance;
    }

    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    public BigDecimal getYearToDatePerfomance() {
        return yearToDatePerfomance;
    }

    public void setYearToDatePerfomance(BigDecimal yearToDatePerfomance) {
        this.yearToDatePerfomance = yearToDatePerfomance;
    }
}
