package com.mali.crypfy.gateway.services.indexmanager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyPerfomanceJSON implements Serializable {
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
