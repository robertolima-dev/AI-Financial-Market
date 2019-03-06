package com.crypfy.core.entity;

public class OptResult {

    private double periodMean;
    private double totalReturn;
    private double maxDD;
    private double inferiorL;
    private double superiorL;
    private double comparative;

    public double getPeriodMean() {
        return periodMean;
    }

    public void setPeriodMean(double periodMean) {
        this.periodMean = periodMean;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }

    public double getMaxDD() {
        return maxDD;
    }

    public void setMaxDD(double maxDD) {
        this.maxDD = maxDD;
    }

    public double getInferiorL() {
        return inferiorL;
    }

    public void setInferiorL(double inferiorL) {
        this.inferiorL = inferiorL;
    }

    public double getSuperiorL() {
        return superiorL;
    }

    public void setSuperiorL(double superiorL) {
        this.superiorL = superiorL;
    }

    public double getComparative() {
        return comparative;
    }

    public void setComparative(double comparative) {
        this.comparative = comparative;
    }
}
