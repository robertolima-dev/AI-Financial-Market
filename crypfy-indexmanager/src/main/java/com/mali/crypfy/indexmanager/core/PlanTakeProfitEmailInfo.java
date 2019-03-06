package com.mali.crypfy.indexmanager.core;

public class PlanTakeProfitEmailInfo {

    public PlanTakeProfitEmailInfo(String planName, String startDate, String endDate, String amount) {
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    private String planName;
    private String startDate;
    private String endDate;
    private String amount;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
