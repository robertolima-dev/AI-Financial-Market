package com.mali.exchanger.communication.impl.binance.sub.entity;

public class MarketDetails {
    private String market;
    private double priceMinSize;
    private double priceStep;
    private double quantityMinSize;
    private double quantityStep;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public double getPriceMinSize() {
        return priceMinSize;
    }

    public void setPriceMinSize(double priceMinSize) {
        this.priceMinSize = priceMinSize;
    }

    public double getPriceStep() {
        return priceStep;
    }

    public void setPriceStep(double priceStep) {
        this.priceStep = priceStep;
    }

    public double getQuantityMinSize() {
        return quantityMinSize;
    }

    public void setQuantityMinSize(double quantityMinSize) {
        this.quantityMinSize = quantityMinSize;
    }

    public double getQuantityStep() {
        return quantityStep;
    }

    public void setQuantityStep(double quantityStep) {
        this.quantityStep = quantityStep;
    }
}
