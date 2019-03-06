package com.mali.crypfy.indexmanager.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mali.crypfy.indexmanager.persistence.enumeration.PlanRequestProfitStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class PlanTakeProfitRequest implements Serializable {

    private Integer idplanTakeProfitRequest;
    private UserPlan userPlan;
    private BigDecimal amount;
    private String email;
    private Date created;
    private Date indexDate;
    private Date lastUpdated;
    private PlanRequestProfitStatus status;
    private Integer iduserPlan;
    private String failedReason;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplan_take_profit_request")
    public Integer getIdplanTakeProfitRequest() {
        return idplanTakeProfitRequest;
    }

    public void setIdplanTakeProfitRequest(Integer idplanTakeProfitRequest) {
        this.idplanTakeProfitRequest = idplanTakeProfitRequest;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser_plan")
    public UserPlan getUserPlan() {
        return userPlan;
    }

    public void setUserPlan(UserPlan userPlan) {
        this.userPlan = userPlan;
    }

    @Column(name = "amount",precision = 10,scale = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "email",length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "index_date")
    public Date getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(Date indexDate) {
        this.indexDate = indexDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public PlanRequestProfitStatus getStatus() {
        return status;
    }

    public void setStatus(PlanRequestProfitStatus status) {
        this.status = status;
    }

    @Column(name = "iduser_plan",insertable = false,updatable = false)
    public Integer getIduserPlan() {
        return iduserPlan;
    }

    public void setIduserPlan(Integer iduserPlan) {
        this.iduserPlan = iduserPlan;
    }

    @Column(name = "failed_reason",length = 3000)
    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }
}
