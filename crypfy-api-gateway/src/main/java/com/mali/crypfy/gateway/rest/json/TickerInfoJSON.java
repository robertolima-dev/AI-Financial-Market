package com.mali.crypfy.gateway.rest.json;

import java.math.BigDecimal;

public class TickerInfoJSON {

    public TickerInfoJSON(String label, BigDecimal value, BigDecimal percent, String secondLabel, BigDecimal secondValue, BigDecimal secondPercent) {
        this.label = label;
        this.value = value;
        this.percent = percent;
        this.secondLabel = secondLabel;
        this.secondValue = secondValue;
        this.secondPercent = secondPercent;
    }

    private String secondLabel;
    private BigDecimal secondValue;
    private String label;
    private BigDecimal value;
    private BigDecimal percent;
    private BigDecimal secondPercent;

    public String getSecondLabel() {
        return secondLabel;
    }

    public void setSecondLabel(String secondLabel) {
        this.secondLabel = secondLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(BigDecimal secondValue) {
        this.secondValue = secondValue;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public BigDecimal getSecondPercent() {
        return secondPercent;
    }

    public void setSecondPercent(BigDecimal secondPercent) {
        this.secondPercent = secondPercent;
    }
}
