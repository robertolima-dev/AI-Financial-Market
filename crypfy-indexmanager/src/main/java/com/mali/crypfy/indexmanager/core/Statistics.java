package com.mali.crypfy.indexmanager.core;

import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;

import java.math.BigDecimal;
import java.util.List;

public class Statistics {

    private List<PlanStatistics> plans;
    private BigDecimal maliCustody;
    private BigDecimal feesTotal;

    public List<PlanStatistics> getPlans() {
        return plans;
    }

    public void setPlans(List<PlanStatistics> plans) {
        this.plans = plans;
    }

    public BigDecimal getMaliCustody() {
        return maliCustody;
    }

    public void setMaliCustody(BigDecimal maliCustody) {
        this.maliCustody = maliCustody;
    }

    public BigDecimal getFeesTotal() {
        return feesTotal;
    }

    public void setFeesTotal(BigDecimal feesTotal) {
        this.feesTotal = feesTotal;
    }
}
