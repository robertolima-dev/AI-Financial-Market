package com.mali.crypfy.indexmanager.core;

import java.math.BigDecimal;

public class MonthlyPerfomance {
    private Integer month;
    private String monthName;
    private BigDecimal perfomance;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public BigDecimal getPerfomance() {
        return perfomance;
    }

    public void setPerfomance(BigDecimal perfomance) {
        this.perfomance = perfomance;
    }
}
