package com.mali.crypfy.indexmanager.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class IndexPlan implements Serializable {

    public static IndexPlan copy(IndexPlan toCopyObject) {
        IndexPlan newTakeProfitBreakPoint = new IndexPlan();
        newTakeProfitBreakPoint.setIdindexPlan(toCopyObject.getIdindexPlan());
        newTakeProfitBreakPoint.setIdplan(toCopyObject.getIdplan());
        newTakeProfitBreakPoint.setIndex(toCopyObject.getIndex());
        newTakeProfitBreakPoint.setPlan(toCopyObject.getPlan());
        newTakeProfitBreakPoint.setUpdated(toCopyObject.getUpdated());
        return newTakeProfitBreakPoint;
    }

    private Integer idindexPlan;
    private BigDecimal index;
    private Date updated;
    private Plan plan;
    private Integer idplan;

    //transient
    private BigDecimal takeProfitAmount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idindex_plan")
    public Integer getIdindexPlan() {
        return idindexPlan;
    }

    public void setIdindexPlan(Integer idindexPlan) {
        this.idindexPlan = idindexPlan;
    }

    @Column(name = "index",precision = 10, scale = 3)
    public BigDecimal getIndex() {
        return index;
    }

    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idplan")
    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Column(name = "idplan",insertable = false,updatable = false)
    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    @Transient
    public BigDecimal getTakeProfitAmount() {
        return takeProfitAmount;
    }

    public void setTakeProfitAmount(BigDecimal takeProfitAmount) {
        this.takeProfitAmount = takeProfitAmount;
    }

    @Transient
    public boolean hasTakeProfit() {
        return (this.takeProfitAmount == null) ? false : true;
    }
}
