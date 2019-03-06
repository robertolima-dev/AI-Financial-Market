package com.mali.crypfy.gateway.services.indexmanager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPlanJSON {

    private Integer iduserPlan;
    private String email;
    private Integer idplan;
    private PlanJSON plan;
    private Date startDate;
    private Date endDate;
    private BigDecimal initialBalance;
    private String status;
    private Integer duration;
    private String planName;
    private BigDecimal inputFee;

    private BigDecimal totalProfitPercent;
    private BigDecimal totalProfit;
    private BigDecimal totalProfitWithdraw;
    private BigDecimal sumWithdraw;
    private BigDecimal currentBalance;
    private BigDecimal totalFeeAmount;
    private BigDecimal firstIndex;
    private BigDecimal availableProfit;

    private BigDecimal totalProfitCurrentMonth;
    private BigDecimal totalProfitLastMonth;
    private BigDecimal totalProfitLastThreeMonth;
    private BigDecimal totalProfitToday;
    private BigDecimal totalProfitYesterday;
    private BigDecimal totalProfitCurrentWeek;
    private BigDecimal totalProfitLastWeek;

    private BigDecimal totalProfitPercentLastThreeMonth = BigDecimal.ZERO;
    private BigDecimal totalProfitPercentLastMonth = BigDecimal.ZERO;
    private BigDecimal totalProfitPercentCurrentMonth = BigDecimal.ZERO;
    private BigDecimal totalProfitPercentLastWeek = BigDecimal.ZERO;
    private BigDecimal totalProfitPercentCurrentWeek = BigDecimal.ZERO;
    private BigDecimal totalProfitPercentYesterday = BigDecimal.ZERO;
    private BigDecimal totalProfitPercentToday = BigDecimal.ZERO;

    private BigDecimal baseAmountCurrentMonth = BigDecimal.ZERO;
    private BigDecimal baseAmountLastMonth = BigDecimal.ZERO;
    private BigDecimal baseAmountLastThreeMonth = BigDecimal.ZERO;
    private BigDecimal baseAmountToday = BigDecimal.ZERO;
    private BigDecimal baseAmountYesterday = BigDecimal.ZERO;
    private BigDecimal baseAmountCurrentWeek = BigDecimal.ZERO;
    private BigDecimal baseAmountLastWeek = BigDecimal.ZERO;

    private List<BigDecimal> evolutionPoints;


    public Integer getIduserPlan() {
        return iduserPlan;
    }

    public void setIduserPlan(Integer iduserPlan) {
        this.iduserPlan = iduserPlan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PlanJSON getPlan() {
        return plan;
    }

    public void setPlan(PlanJSON plan) {
        this.plan = plan;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public BigDecimal getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(BigDecimal firstIndex) {
        this.firstIndex = firstIndex;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }


    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    public BigDecimal getTotalProfitPercent() {
        return totalProfitPercent;
    }

    public void setTotalProfitPercent(BigDecimal totalProfitPercent) {
        this.totalProfitPercent = totalProfitPercent;
    }

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

    public BigDecimal getSumWithdraw() {
        return sumWithdraw;
    }

    public void setSumWithdraw(BigDecimal sumWithdraw) {
        this.sumWithdraw = sumWithdraw;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(BigDecimal totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
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

    public BigDecimal getAvailableProfit() {
        return availableProfit;
    }

    public void setAvailableProfit(BigDecimal availableProfit) {
        this.availableProfit = availableProfit;
    }

    public BigDecimal getInputFee() {
        return inputFee;
    }

    public void setInputFee(BigDecimal inputFee) {
        this.inputFee = inputFee;
    }

    public BigDecimal getTotalProfitToday() {
        return totalProfitToday;
    }

    public void setTotalProfitToday(BigDecimal totalProfitToday) {
        this.totalProfitToday = totalProfitToday;
    }

    public BigDecimal getTotalProfitYesterday() {
        return totalProfitYesterday;
    }

    public void setTotalProfitYesterday(BigDecimal totalProfitYesterday) {
        this.totalProfitYesterday = totalProfitYesterday;
    }

    public BigDecimal getTotalProfitCurrentWeek() {
        return totalProfitCurrentWeek;
    }

    public void setTotalProfitCurrentWeek(BigDecimal totalProfitCurrentWeek) {
        this.totalProfitCurrentWeek = totalProfitCurrentWeek;
    }

    public BigDecimal getTotalProfitLastWeek() {
        return totalProfitLastWeek;
    }

    public void setTotalProfitLastWeek(BigDecimal totalProfitLastWeek) {
        this.totalProfitLastWeek = totalProfitLastWeek;
    }

    public BigDecimal getTotalProfitPercentLastThreeMonth() {
        return totalProfitPercentLastThreeMonth;
    }

    public void setTotalProfitPercentLastThreeMonth(BigDecimal totalProfitPercentLastThreeMonth) {
        this.totalProfitPercentLastThreeMonth = totalProfitPercentLastThreeMonth;
    }

    public BigDecimal getTotalProfitPercentLastMonth() {
        return totalProfitPercentLastMonth;
    }

    public void setTotalProfitPercentLastMonth(BigDecimal totalProfitPercentLastMonth) {
        this.totalProfitPercentLastMonth = totalProfitPercentLastMonth;
    }

    public BigDecimal getTotalProfitPercentCurrentMonth() {
        return totalProfitPercentCurrentMonth;
    }

    public void setTotalProfitPercentCurrentMonth(BigDecimal totalProfitPercentCurrentMonth) {
        this.totalProfitPercentCurrentMonth = totalProfitPercentCurrentMonth;
    }

    public BigDecimal getTotalProfitPercentLastWeek() {
        return totalProfitPercentLastWeek;
    }

    public void setTotalProfitPercentLastWeek(BigDecimal totalProfitPercentLastWeek) {
        this.totalProfitPercentLastWeek = totalProfitPercentLastWeek;
    }

    public BigDecimal getTotalProfitPercentCurrentWeek() {
        return totalProfitPercentCurrentWeek;
    }

    public void setTotalProfitPercentCurrentWeek(BigDecimal totalProfitPercentCurrentWeek) {
        this.totalProfitPercentCurrentWeek = totalProfitPercentCurrentWeek;
    }

    public BigDecimal getTotalProfitPercentYesterday() {
        return totalProfitPercentYesterday;
    }

    public void setTotalProfitPercentYesterday(BigDecimal totalProfitPercentYesterday) {
        this.totalProfitPercentYesterday = totalProfitPercentYesterday;
    }

    public BigDecimal getTotalProfitPercentToday() {
        return totalProfitPercentToday;
    }

    public void setTotalProfitPercentToday(BigDecimal totalProfitPercentToday) {
        this.totalProfitPercentToday = totalProfitPercentToday;
    }

    public BigDecimal getBaseAmountCurrentMonth() {
        return baseAmountCurrentMonth;
    }

    public void setBaseAmountCurrentMonth(BigDecimal baseAmountCurrentMonth) {
        this.baseAmountCurrentMonth = baseAmountCurrentMonth;
    }

    public BigDecimal getBaseAmountLastMonth() {
        return baseAmountLastMonth;
    }

    public void setBaseAmountLastMonth(BigDecimal baseAmountLastMonth) {
        this.baseAmountLastMonth = baseAmountLastMonth;
    }

    public BigDecimal getBaseAmountLastThreeMonth() {
        return baseAmountLastThreeMonth;
    }

    public void setBaseAmountLastThreeMonth(BigDecimal baseAmountLastThreeMonth) {
        this.baseAmountLastThreeMonth = baseAmountLastThreeMonth;
    }

    public BigDecimal getBaseAmountToday() {
        return baseAmountToday;
    }

    public void setBaseAmountToday(BigDecimal baseAmountToday) {
        this.baseAmountToday = baseAmountToday;
    }

    public BigDecimal getBaseAmountYesterday() {
        return baseAmountYesterday;
    }

    public void setBaseAmountYesterday(BigDecimal baseAmountYesterday) {
        this.baseAmountYesterday = baseAmountYesterday;
    }

    public BigDecimal getBaseAmountCurrentWeek() {
        return baseAmountCurrentWeek;
    }

    public void setBaseAmountCurrentWeek(BigDecimal baseAmountCurrentWeek) {
        this.baseAmountCurrentWeek = baseAmountCurrentWeek;
    }

    public BigDecimal getBaseAmountLastWeek() {
        return baseAmountLastWeek;
    }

    public void setBaseAmountLastWeek(BigDecimal baseAmountLastWeek) {
        this.baseAmountLastWeek = baseAmountLastWeek;
    }

    public List<BigDecimal> getEvolutionPoints() {
        return evolutionPoints;
    }

    public void setEvolutionPoints(List<BigDecimal> evolutionPoints) {
        this.evolutionPoints = evolutionPoints;
    }
}
