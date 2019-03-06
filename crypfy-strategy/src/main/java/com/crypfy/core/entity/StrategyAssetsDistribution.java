package com.crypfy.core.entity;

import java.util.List;

public class StrategyAssetsDistribution {
    private String date;
    private double volValue;
    private List<Asset> assets;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public double getVolValue() {
        return volValue;
    }

    public void setVolValue(double volValue) {
        this.volValue = volValue;
    }
}
