package com.mali.crypfy.gateway.services.indexmanager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanTakeProfitRequestJSON {

    private Integer idplanTakeProfitRequest;
    private BigDecimal amount;
    private String email;
    private UserPlanJSON userPlan;
    private Date created;
    private Date indexDate;
    private Date lastUpdated;
    private String status;
    private Integer iduserPlan;
    private String failedReason;

    public Integer getIdplanTakeProfitRequest() {
        return idplanTakeProfitRequest;
    }

    public void setIdplanTakeProfitRequest(Integer idplanTakeProfitRequest) {
        this.idplanTakeProfitRequest = idplanTakeProfitRequest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(Date indexDate) {
        this.indexDate = indexDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIduserPlan() {
        return iduserPlan;
    }

    public void setIduserPlan(Integer iduserPlan) {
        this.iduserPlan = iduserPlan;
    }

    public UserPlanJSON getUserPlan() {
        return userPlan;
    }

    public void setUserPlan(UserPlanJSON userPlan) {
        this.userPlan = userPlan;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }
}
