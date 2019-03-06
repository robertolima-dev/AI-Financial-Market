package com.mali.crypfy.clustering.snapshots.entity;

import java.util.Date;
import java.util.List;

public class SnapshotData {

    private String coinName;
    private List<LinearizedCoinHistory> candles;
    private double[] shape;
    private double[] volumeShape;
    private double[] imgShape;
    private double[] changes;
    private double[] possibleTPs;
    private double[] possibleSLs;
    private Date date;

    public double[] getShape() {
        return shape;
    }

    public void setShape(double[] shape) {
        this.shape = shape;
    }

    public double[] getChanges() {
        return changes;
    }

    public void setChanges(double[] changes) {
        this.changes = changes;
    }

    public double[] getPossibleTPs() {
        return possibleTPs;
    }

    public void setPossibleTPs(double[] possibleTPs) {
        this.possibleTPs = possibleTPs;
    }

    public double[] getPossibleSLs() {
        return possibleSLs;
    }

    public void setPossibleSLs(double[] possibleSLs) {
        this.possibleSLs = possibleSLs;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public List<LinearizedCoinHistory> getCandles() {
        return candles;
    }

    public void setCandles(List<LinearizedCoinHistory> candles) {
        this.candles = candles;
    }

    public double[] getImgShape() {
        return imgShape;
    }

    public void setImgShape(double[] imgShape) {
        this.imgShape = imgShape;
    }

    public double[] getVolumeShape() {
        return volumeShape;
    }

    public void setVolumeShape(double[] volumeShape) {
        this.volumeShape = volumeShape;
    }
}
