package com.mali.exchanger.communication.impl.foxbit.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderHistoryDetails {

    private Object[] ordListGrp;
    private String[] columns;

    @JsonProperty(value = "OrdListGrp")
    public Object[] getOrdListGrp() {
        return ordListGrp;
    }

    public void setOrdListGrp(Object[] ordListGrp) {
        this.ordListGrp = ordListGrp;
    }

    @JsonProperty(value = "Columns")
    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }
}
