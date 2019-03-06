package com.mali.crypfy.gateway.services.indexmanager.json;

import java.math.BigDecimal;

public class PlanStatisticsJSON {

    private Integer id;
    private String planName;
    private Integer activePlans;
    private BigDecimal planTotalProfit;
    private BigDecimal planTotalCustody;
    private BigDecimal planTotalProfitCurrentMonth;
    private BigDecimal planTotalFeeCurrentMonth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getActivePlans() {
        return activePlans;
    }

    public void setActivePlans(Integer activePlans) {
        this.activePlans = activePlans;
    }

    public BigDecimal getPlanTotalProfit() {
        return planTotalProfit;
    }

    public void setPlanTotalProfit(BigDecimal planTotalProfit) {
        this.planTotalProfit = planTotalProfit;
    }

    public BigDecimal getPlanTotalCustody() {
        return planTotalCustody;
    }

    public void setPlanTotalCustody(BigDecimal planTotalCustody) {
        this.planTotalCustody = planTotalCustody;
    }

    public BigDecimal getPlanTotalProfitCurrentMonth() {
        return planTotalProfitCurrentMonth;
    }

    public void setPlanTotalProfitCurrentMonth(BigDecimal planTotalProfitCurrentMonth) {
        this.planTotalProfitCurrentMonth = planTotalProfitCurrentMonth;
    }

    public BigDecimal getPlanTotalFeeCurrentMonth() {
        return planTotalFeeCurrentMonth;
    }

    public void setPlanTotalFeeCurrentMonth(BigDecimal planTotalFeeCurrentMonth) {
        this.planTotalFeeCurrentMonth = planTotalFeeCurrentMonth;
    }
}
