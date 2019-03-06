package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderHistoryDetails {
    private String orderUuid;
    private String marketName;
    private String openTime;
    private String orderType;
    private double price;
    private double quantity;
    private double comission;
    private boolean isConditional;
    private String condition;
    private double conditionTarget;
    private String closedTime;
    private double costs;

    @JsonProperty("OrderUuid")
    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    @JsonProperty("Exchange")
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @JsonProperty("TimeStamp")
    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("OrderType")
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @JsonProperty("Limit")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("Quantity")
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("Comission")
    public double getComission() {
        return comission;
    }

    public void setComission(double comission) {
        this.comission = comission;
    }

    @JsonProperty("IsConditional")
    public boolean isConditional() {
        return isConditional;
    }

    public void setConditional(boolean conditional) {
        isConditional = conditional;
    }

    @JsonProperty("Condition")
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @JsonProperty("ConditionTarget")
    public double getConditionTarget() {
        return conditionTarget;
    }

    public void setConditionTarget(double conditionTarget) {
        this.conditionTarget = conditionTarget;
    }

    @JsonProperty("Closed")
    public String getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(String closedTime) {
        this.closedTime = closedTime;
    }

    @JsonProperty("Price")
    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }
}
