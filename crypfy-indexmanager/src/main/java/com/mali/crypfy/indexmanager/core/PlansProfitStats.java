package com.mali.crypfy.indexmanager.core;

import java.io.Serializable;
import java.math.BigDecimal;

public class PlansProfitStats implements Serializable {

    private BigDecimal totalProfit;
    private BigDecimal totalProfitWithdraw;
    private BigDecimal totalProfitCurrentMonth;
    private BigDecimal totalProfitLastMonth;
    private BigDecimal totalProfitLastThreeMonth;

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalProfitWithdraw() {
        return totalProfitWithdraw;
    }

    public void setTotalProfitWithdraw(BigDecimal totalProfitWithdraw) {
        this.totalProfitWithdraw = totalProfitWithdraw;
    }

    public BigDecimal getTotalProfitCurrentMonth() {
        return totalProfitCurrentMonth;
    }

    public void setTotalProfitCurrentMonth(BigDecimal totalProfitCurrentMonth) {
        this.totalProfitCurrentMonth = totalProfitCurrentMonth;
    }

    public BigDecimal getTotalProfitLastMonth() {
        return totalProfitLastMonth;
    }

    public void setTotalProfitLastMonth(BigDecimal totalProfitLastMonth) {
        this.totalProfitLastMonth = totalProfitLastMonth;
    }

    public BigDecimal getTotalProfitLastThreeMonth() {
        return totalProfitLastThreeMonth;
    }

    public void setTotalProfitLastThreeMonth(BigDecimal totalProfitLastThreeMonth) {
        this.totalProfitLastThreeMonth = totalProfitLastThreeMonth;
    }
}
