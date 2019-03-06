package com.mali.crypfy.indexmanager.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mali.crypfy.indexmanager.core.BalanceIndex;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Entity
public class UserPlan implements Serializable {

    private Integer iduserPlan;
    private String email;
    private Plan plan;
    private Date startDate;
    private Date endDate;
    private Date created;
    private BigDecimal initialBalance;
    private UserPlanStatus status;
    private Integer duration;
    private BigDecimal perfomanceFee;
    private Integer idplan;
    private String planName;
    private BigDecimal inputFee;

    private BigDecimal oneHundred = new BigDecimal(100);

    //transients
    private BigDecimal totalProfitPercent = BigDecimal.ZERO;
    private BigDecimal totalProfit = BigDecimal.ZERO;
    private BigDecimal totalProfitWithdraw = BigDecimal.ZERO;
    private BigDecimal sumWithdraw = BigDecimal.ZERO;
    private BigDecimal currentBalance = BigDecimal.ZERO;
    private BigDecimal totalFeeAmount = BigDecimal.ZERO;
    private BigDecimal firstIndex = BigDecimal.ZERO;

    private BigDecimal totalProfitCurrentMonth = BigDecimal.ZERO;
    private BigDecimal totalProfitLastMonth = BigDecimal.ZERO;
    private BigDecimal totalProfitLastThreeMonth = BigDecimal.ZERO;
    private BigDecimal totalProfitToday = BigDecimal.ZERO;
    private BigDecimal totalProfitYesterday = BigDecimal.ZERO;
    private BigDecimal totalProfitCurrentWeek = BigDecimal.ZERO;
    private BigDecimal totalProfitLastWeek = BigDecimal.ZERO;

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

    private BigDecimal totalFeeCurrentMonth;
    private BigDecimal availableProfit = BigDecimal.ZERO;

    private List<BalanceIndex> indexes;
    private List<BigDecimal> evolutionPoints;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser_plan")
    public Integer getIduserPlan() {
        return iduserPlan;
    }

    public void setIduserPlan(Integer iduserPlan) {
        this.iduserPlan = iduserPlan;
    }

    @Column(name = "email",length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idplan")
    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "initial_balance",precision = 10, scale = 2)
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status",length = 100)
    public UserPlanStatus getStatus() {
        return status;
    }

    public void setStatus(UserPlanStatus status) {
        this.status = status;
    }

    @Column(name = "duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Column(name = "perfomance_fee",precision = 10, scale = 2)
    public BigDecimal getPerfomanceFee() {
        return perfomanceFee;
    }

    public void setPerfomanceFee(BigDecimal perfomanceFee) {
        this.perfomanceFee = perfomanceFee;
    }

    @Transient
    public BigDecimal getTotalProfitPercent() {
        return totalProfitPercent;
    }

    public void setTotalProfitPercent(BigDecimal totalProfitPercent) {
        this.totalProfitPercent = totalProfitPercent;
    }

    @Transient
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Transient
    public BigDecimal getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(BigDecimal firstIndex) {
        this.firstIndex = firstIndex;
    }

    @Column(name = "idplan",insertable = false,updatable = false)
    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    @Column(name = "plan_name")
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Transient
    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    @Transient
    public BigDecimal getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(BigDecimal totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Transient
    public BigDecimal getSumWithdraw() {
        return sumWithdraw;
    }

    public void setSumWithdraw(BigDecimal sumWithdraw) {
        this.sumWithdraw = sumWithdraw;
    }

    @Transient
    public BigDecimal getTotalProfitWithdraw() {
        return totalProfitWithdraw;
    }

    public void setTotalProfitWithdraw(BigDecimal totalProfitWithdraw) {
        this.totalProfitWithdraw = totalProfitWithdraw;
    }

    @Transient
    public BigDecimal getTotalProfitCurrentMonth() {
        return totalProfitCurrentMonth;
    }

    public void setTotalProfitCurrentMonth(BigDecimal totalProfitCurrentMonth) {
        this.totalProfitCurrentMonth = totalProfitCurrentMonth;
    }

    @Transient
    public BigDecimal getTotalProfitLastMonth() {
        return totalProfitLastMonth;
    }

    public void setTotalProfitLastMonth(BigDecimal totalProfitLastMonth) {
        this.totalProfitLastMonth = totalProfitLastMonth;
    }

    @Transient
    public BigDecimal getTotalProfitLastThreeMonth() {
        return totalProfitLastThreeMonth;
    }

    public void setTotalProfitLastThreeMonth(BigDecimal totalProfitLastThreeMonth) {
        this.totalProfitLastThreeMonth = totalProfitLastThreeMonth;
    }

    @Transient
    public BigDecimal getAvailableProfit() {
        return availableProfit;
    }

    public void setAvailableProfit(BigDecimal availableProfit) {
        this.availableProfit = availableProfit;
    }

    @Column(name = "input_fee",precision = 10, scale = 2)
    public BigDecimal getInputFee() {
        return inputFee;
    }

    public void setInputFee(BigDecimal inputFee) {
        this.inputFee = inputFee;
    }

    @Transient
    public BigDecimal getTotalFeeCurrentMonth() {
        return totalFeeCurrentMonth;
    }

    public void setTotalFeeCurrentMonth(BigDecimal totalFeeCurrentMonth) {
        this.totalFeeCurrentMonth = totalFeeCurrentMonth;

    }

    @Transient
    public List<BalanceIndex> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<BalanceIndex> indexes) {
        this.indexes = indexes;
    }

    @Transient
    public BigDecimal getTotalProfitToday() {
        return totalProfitToday;
    }

    public void setTotalProfitToday(BigDecimal totalProfitToday) {
        this.totalProfitToday = totalProfitToday;
    }

    @Transient
    public BigDecimal getTotalProfitYesterday() {
        return totalProfitYesterday;
    }

    public void setTotalProfitYesterday(BigDecimal totalProfitYesterday) {
        this.totalProfitYesterday = totalProfitYesterday;
    }

    @Transient
    public BigDecimal getTotalProfitCurrentWeek() {
        return totalProfitCurrentWeek;
    }

    public void setTotalProfitCurrentWeek(BigDecimal totalProfitCurrentWeek) {
        this.totalProfitCurrentWeek = totalProfitCurrentWeek;
    }

    @Transient
    public BigDecimal getTotalProfitLastWeek() {
        return totalProfitLastWeek;
    }

    public void setTotalProfitLastWeek(BigDecimal totalProfitLastWeek) {
        this.totalProfitLastWeek = totalProfitLastWeek;
    }

    @Transient
    public BigDecimal getTotalProfitPercentLastThreeMonth() {
        return totalProfitPercentLastThreeMonth;
    }

    public void setTotalProfitPercentLastThreeMonth(BigDecimal totalProfitPercentLastThreeMonth) {
        this.totalProfitPercentLastThreeMonth = totalProfitPercentLastThreeMonth;
    }

    @Transient
    public BigDecimal getTotalProfitPercentLastMonth() {
        return totalProfitPercentLastMonth;
    }

    public void setTotalProfitPercentLastMonth(BigDecimal totalProfitPercentLastMonth) {
        this.totalProfitPercentLastMonth = totalProfitPercentLastMonth;
    }

    @Transient
    public BigDecimal getTotalProfitPercentCurrentMonth() {
        return totalProfitPercentCurrentMonth;
    }

    public void setTotalProfitPercentCurrentMonth(BigDecimal totalProfitPercentCurrentMonth) {
        this.totalProfitPercentCurrentMonth = totalProfitPercentCurrentMonth;
    }

    @Transient
    public BigDecimal getTotalProfitPercentLastWeek() {
        return totalProfitPercentLastWeek;
    }

    public void setTotalProfitPercentLastWeek(BigDecimal totalProfitPercentLastWeek) {
        this.totalProfitPercentLastWeek = totalProfitPercentLastWeek;
    }

    @Transient
    public BigDecimal getTotalProfitPercentCurrentWeek() {
        return totalProfitPercentCurrentWeek;
    }

    public void setTotalProfitPercentCurrentWeek(BigDecimal totalProfitPercentCurrentWeek) {
        this.totalProfitPercentCurrentWeek = totalProfitPercentCurrentWeek;
    }

    @Transient
    public BigDecimal getTotalProfitPercentYesterday() {
        return totalProfitPercentYesterday;
    }

    public void setTotalProfitPercentYesterday(BigDecimal totalProfitPercentYesterday) {
        this.totalProfitPercentYesterday = totalProfitPercentYesterday;
    }

    @Transient
    public BigDecimal getTotalProfitPercentToday() {
        return totalProfitPercentToday;
    }

    public void setTotalProfitPercentToday(BigDecimal totalProfitPercentToday) {
        this.totalProfitPercentToday = totalProfitPercentToday;
    }

    @Transient
    public BigDecimal getBaseAmountCurrentMonth() {
        return baseAmountCurrentMonth;
    }

    public void setBaseAmountCurrentMonth(BigDecimal baseAmountCurrentMonth) {
        this.baseAmountCurrentMonth = baseAmountCurrentMonth;
    }

    @Transient
    public BigDecimal getBaseAmountLastMonth() {
        return baseAmountLastMonth;
    }

    public void setBaseAmountLastMonth(BigDecimal baseAmountLastMonth) {
        this.baseAmountLastMonth = baseAmountLastMonth;
    }

    @Transient
    public BigDecimal getBaseAmountLastThreeMonth() {
        return baseAmountLastThreeMonth;
    }

    public void setBaseAmountLastThreeMonth(BigDecimal baseAmountLastThreeMonth) {
        this.baseAmountLastThreeMonth = baseAmountLastThreeMonth;
    }

    @Transient
    public BigDecimal getBaseAmountToday() {
        return baseAmountToday;
    }

    public void setBaseAmountToday(BigDecimal baseAmountToday) {
        this.baseAmountToday = baseAmountToday;
    }

    @Transient
    public BigDecimal getBaseAmountYesterday() {
        return baseAmountYesterday;
    }

    public void setBaseAmountYesterday(BigDecimal baseAmountYesterday) {
        this.baseAmountYesterday = baseAmountYesterday;
    }

    @Transient
    public BigDecimal getBaseAmountCurrentWeek() {
        return baseAmountCurrentWeek;
    }

    public void setBaseAmountCurrentWeek(BigDecimal baseAmountCurrentWeek) {
        this.baseAmountCurrentWeek = baseAmountCurrentWeek;
    }

    @Transient
    public BigDecimal getBaseAmountLastWeek() {
        return baseAmountLastWeek;
    }

    public void setBaseAmountLastWeek(BigDecimal baseAmountLastWeek) {
        this.baseAmountLastWeek = baseAmountLastWeek;
    }

    @Transient
    public void calcProfitPercentPeriods() {
        if(baseAmountLastThreeMonth.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentLastThreeMonth = this.totalProfitLastThreeMonth.divide(baseAmountLastThreeMonth,4, RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
        if(baseAmountLastMonth.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentLastMonth = this.totalProfitLastMonth.divide(baseAmountLastMonth,4, RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
        if(baseAmountCurrentMonth.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentCurrentMonth = this.totalProfitCurrentMonth.divide(baseAmountCurrentMonth,4, RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
            this.totalFeeCurrentMonth = (this.totalProfitCurrentMonth.divide(BigDecimal.ONE.subtract(this.perfomanceFee),4,RoundingMode.DOWN)).subtract(this.totalProfitCurrentMonth);
            this.totalFeeCurrentMonth = this.totalFeeCurrentMonth.compareTo(BigDecimal.ZERO) >= 0 ? this.totalFeeCurrentMonth : BigDecimal.ZERO;
        if(baseAmountCurrentWeek.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentCurrentWeek = this.totalProfitCurrentWeek.divide(baseAmountCurrentWeek,4, RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
        if(baseAmountLastWeek.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentLastWeek = this.totalProfitLastWeek.divide(baseAmountLastWeek,4, RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
        if(baseAmountYesterday.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentYesterday = this.totalProfitYesterday.divide(baseAmountYesterday,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
        if(baseAmountToday.compareTo(BigDecimal.ZERO) > 0)
            this.totalProfitPercentToday = this.totalProfitToday.divide(baseAmountToday,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN);
    }

    @Transient
    public List<BigDecimal> getEvolutionPoints() {
        return evolutionPoints;
    }

    public void setEvolutionPoints(List<BigDecimal> evolutionPoints) {
        this.evolutionPoints = evolutionPoints;
    }
}
