package com.mali.crypfy.gateway.rest.json;

import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;

import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardDataJSON {

    private BigDecimal totalCustody;
    private BigDecimal totalProfit;
    private BigDecimal monthProfit;
    private BigDecimal lineaCustody;
    private BigDecimal audaceCustody;
    private BigDecimal lineaPercent;
    private BigDecimal audacePercent;
    private BigDecimal monthFees;
    private BigDecimal monthDepositTotal;
    private BigDecimal maliMoney;
    private List<DepositWithdrawRequestlBrlJSON> deposits;
    private List<DepositWithdrawRequestlBrlJSON> withdraws;
    private Integer totalPlans;

    public BigDecimal getTotalCustody() {
        return totalCustody;
    }

    public void setTotalCustody(BigDecimal totalCustody) {
        this.totalCustody = totalCustody;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getMonthProfit() {
        return monthProfit;
    }

    public void setMonthProfit(BigDecimal monthProfit) {
        this.monthProfit = monthProfit;
    }

    public BigDecimal getLineaCustody() {
        return lineaCustody;
    }

    public void setLineaCustody(BigDecimal lineaCustody) {
        this.lineaCustody = lineaCustody;
    }

    public BigDecimal getAudaceCustody() {
        return audaceCustody;
    }

    public void setAudaceCustody(BigDecimal audaceCustody) {
        this.audaceCustody = audaceCustody;
    }

    public BigDecimal getLineaPercent() {
        return lineaPercent;
    }

    public void setLineaPercent(BigDecimal lineaPercent) {
        this.lineaPercent = lineaPercent;
    }

    public BigDecimal getAudacePercent() {
        return audacePercent;
    }

    public void setAudacePercent(BigDecimal audacePercent) {
        this.audacePercent = audacePercent;
    }

    public BigDecimal getMonthFees() {
        return monthFees;
    }

    public void setMonthFees(BigDecimal monthFees) {
        this.monthFees = monthFees;
    }

    public BigDecimal getMonthDepositTotal() {
        return monthDepositTotal;
    }

    public void setMonthDepositTotal(BigDecimal monthDepositTotal) {
        this.monthDepositTotal = monthDepositTotal;
    }

    public BigDecimal getMaliMoney() {
        return maliMoney;
    }

    public void setMaliMoney(BigDecimal maliMoney) {
        this.maliMoney = maliMoney;
    }

    public List<DepositWithdrawRequestlBrlJSON> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<DepositWithdrawRequestlBrlJSON> deposits) {
        this.deposits = deposits;
    }

    public List<DepositWithdrawRequestlBrlJSON> getWithdraws() {
        return withdraws;
    }

    public void setWithdraws(List<DepositWithdrawRequestlBrlJSON> withdraws) {
        this.withdraws = withdraws;
    }

    public Integer getTotalPlans() {
        return totalPlans;
    }

    public void setTotalPlans(Integer totalPlans) {
        this.totalPlans = totalPlans;
    }
}
