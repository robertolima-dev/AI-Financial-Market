package com.mali.crypfy.gateway.rest.json;

import com.mali.crypfy.gateway.services.indexmanager.json.BalancePerDateJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DashboardDataJSON {

    private BigDecimal totalDeposit;
    private Long totalDepositConfirmed;
    private Long totalDepositWaitingApproval;
    private Long totalDepositDenied;
    private Long totalDepositWaitingPhotoUpload;
    private Long totalDepositCancelled;

    private BigDecimal totalWithdraw;
    private Long totalWithdrawConfirmed;
    private Long totalWithdrawWaitingApproval;
    private Long totalWithdrawDenied;
    private Long totalWithdrawProcessing;
    private Long totalWithdrawCancelled;

    private BigDecimal totalProfit;
    private BigDecimal totalAvailableProfit;
    private BigDecimal totalProfitCurrentMonth;
    private BigDecimal totalProfitLastMonth;
    private BigDecimal totalProfitLastThreeMonth;
    private Long totalPlans;
    private Long totalPlansInProgress;
    private Long totalPlansProcessing;
    private Long totalPlansFinished;
    private Long totalPlansAudace;
    private Long totalPlansLinea;
    private List<BalancePerDateJSON> balanceEvolution;
    private List<UserPlanJSON> inProgressPlans;
    private BigDecimal totalBlockedAmount;

    public BigDecimal getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(BigDecimal totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }

    public Long getTotalPlans() {
        return totalPlans;
    }

    public void setTotalPlans(Long totalPlans) {
        this.totalPlans = totalPlans;
    }

    public List<BalancePerDateJSON> getBalanceEvolution() {
        return balanceEvolution;
    }

    public void setBalanceEvolution(List<BalancePerDateJSON> balanceEvolution) {
        this.balanceEvolution = balanceEvolution;
    }

    public List<UserPlanJSON> getInProgressPlans() {
        return inProgressPlans;
    }

    public void setInProgressPlans(List<UserPlanJSON> inProgressPlans) {
        this.inProgressPlans = inProgressPlans;
    }

    public BigDecimal getTotalBlockedAmount() {
        return totalBlockedAmount;
    }

    public void setTotalBlockedAmount(BigDecimal totalBlockedAmount) {
        this.totalBlockedAmount = totalBlockedAmount;
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

    public BigDecimal getTotalAvailableProfit() {
        return totalAvailableProfit;
    }

    public void setTotalAvailableProfit(BigDecimal totalAvailableProfit) {
        this.totalAvailableProfit = totalAvailableProfit;
    }

    public Long getTotalPlansInProgress() {
        return totalPlansInProgress;
    }

    public void setTotalPlansInProgress(Long totalPlansInProgress) {
        this.totalPlansInProgress = totalPlansInProgress;
    }

    public Long getTotalPlansProcessing() {
        return totalPlansProcessing;
    }

    public void setTotalPlansProcessing(Long totalPlansProcessing) {
        this.totalPlansProcessing = totalPlansProcessing;
    }

    public Long getTotalPlansFinished() {
        return totalPlansFinished;
    }

    public void setTotalPlansFinished(Long totalPlansFinished) {
        this.totalPlansFinished = totalPlansFinished;
    }

    public Long getTotalPlansAudace() {
        return totalPlansAudace;
    }

    public void setTotalPlansAudace(Long totalPlansAudace) {
        this.totalPlansAudace = totalPlansAudace;
    }

    public Long getTotalPlansLinea() {
        return totalPlansLinea;
    }

    public void setTotalPlansLinea(Long totalPlansLinea) {
        this.totalPlansLinea = totalPlansLinea;
    }

    public Long getTotalDepositConfirmed() {
        return totalDepositConfirmed;
    }

    public void setTotalDepositConfirmed(Long totalDepositConfirmed) {
        this.totalDepositConfirmed = totalDepositConfirmed;
    }

    public Long getTotalDepositWaitingApproval() {
        return totalDepositWaitingApproval;
    }

    public void setTotalDepositWaitingApproval(Long totalDepositWaitingApproval) {
        this.totalDepositWaitingApproval = totalDepositWaitingApproval;
    }

    public Long getTotalDepositDenied() {
        return totalDepositDenied;
    }

    public void setTotalDepositDenied(Long totalDepositDenied) {
        this.totalDepositDenied = totalDepositDenied;
    }

    public Long getTotalDepositWaitingPhotoUpload() {
        return totalDepositWaitingPhotoUpload;
    }

    public void setTotalDepositWaitingPhotoUpload(Long totalDepositWaitingPhotoUpload) {
        this.totalDepositWaitingPhotoUpload = totalDepositWaitingPhotoUpload;
    }

    public Long getTotalDepositCancelled() {
        return totalDepositCancelled;
    }

    public void setTotalDepositCancelled(Long totalDepositCancelled) {
        this.totalDepositCancelled = totalDepositCancelled;
    }

    public Long getTotalWithdrawConfirmed() {
        return totalWithdrawConfirmed;
    }

    public void setTotalWithdrawConfirmed(Long totalWithdrawConfirmed) {
        this.totalWithdrawConfirmed = totalWithdrawConfirmed;
    }

    public Long getTotalWithdrawWaitingApproval() {
        return totalWithdrawWaitingApproval;
    }

    public void setTotalWithdrawWaitingApproval(Long totalWithdrawWaitingApproval) {
        this.totalWithdrawWaitingApproval = totalWithdrawWaitingApproval;
    }

    public Long getTotalWithdrawDenied() {
        return totalWithdrawDenied;
    }

    public void setTotalWithdrawDenied(Long totalWithdrawDenied) {
        this.totalWithdrawDenied = totalWithdrawDenied;
    }

    public Long getTotalWithdrawProcessing() {
        return totalWithdrawProcessing;
    }

    public void setTotalWithdrawProcessing(Long totalWithdrawProcessing) {
        this.totalWithdrawProcessing = totalWithdrawProcessing;
    }

    public Long getTotalWithdrawCancelled() {
        return totalWithdrawCancelled;
    }

    public void setTotalWithdrawCancelled(Long totalWithdrawCancelled) {
        this.totalWithdrawCancelled = totalWithdrawCancelled;
    }
}
