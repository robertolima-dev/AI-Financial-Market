package com.mali.crypfy.gateway.services.indexmanager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexPlanJSON {

    private Integer idindexPlan;
    private BigDecimal index;
    private Date updated;
    private Integer idplan;
    private BigDecimal takeProfitAmount;

    public Integer getIdindexPlan() {
        return idindexPlan;
    }

    public void setIdindexPlan(Integer idindexPlan) {
        this.idindexPlan = idindexPlan;
    }

    public BigDecimal getIndex() {
        return index;
    }

    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    public BigDecimal getTakeProfitAmount() {
        return takeProfitAmount;
    }

    public void setTakeProfitAmount(BigDecimal takeProfitAmount) {
        this.takeProfitAmount = takeProfitAmount;
    }
}
