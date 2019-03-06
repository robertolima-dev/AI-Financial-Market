package com.mali.crypfy.gateway.services.indexmanager.json;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsJSON {

    private List<PlanStatisticsJSON> plans;
    private BigDecimal maliCustody;
    private BigDecimal feesTotal;

    public List<PlanStatisticsJSON> getPlans() {
        return plans;
    }

    public void setPlans(List<PlanStatisticsJSON> plans) {
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
