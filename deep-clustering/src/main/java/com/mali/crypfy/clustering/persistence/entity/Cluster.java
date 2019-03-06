package com.mali.crypfy.clustering.persistence.entity;

import com.mali.crypfy.clustering.snapshots.entity.SnapshotData;
import org.springframework.data.annotation.Id;
import java.util.List;

public class Cluster {

    private String id;
    private List<SnapshotData> points;
    private Integer clusterInterations;
    private double[] centroid;
    private double[] volumeCentroid;
    private double[] changesMeanWinRate;
    private double[] changesMeanReturn;
    private double[] clusterIndicatedTPs;
    private double[] clusterIndicatedSLs;
    private double[] changesMeanWinRateMomentum;
    private double[] changesMeanReturnMomentum;
    private double[] clusterIndicatedTPsMomentum;
    private double[] clusterIndicatedSLsMomentum;
    private String name;
    private Integer shapeSize;
    private Integer statsSizeFilter;
    private Integer discountedCoins;
    private Integer sizeDenominator;
    private Integer nCoins;
    private double similarity;
    private int timeframe;

    @Id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<SnapshotData> getPoints() {
        return points;
    }

    public void setPoints(List<SnapshotData> points) {
        this.points = points;
    }

    public Integer getClusterInterations() {
        return clusterInterations;
    }

    public void setClusterInterations(Integer clusterInterations) {
        this.clusterInterations = clusterInterations;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    public double[] getChangesMeanWinRate() {
        return changesMeanWinRate;
    }

    public void setChangesMeanWinRate(double[] changesMeanWinRate) {
        this.changesMeanWinRate = changesMeanWinRate;
    }

    public double[] getChangesMeanReturn() {
        return changesMeanReturn;
    }

    public void setChangesMeanReturn(double[] changesMeanReturn) {
        this.changesMeanReturn = changesMeanReturn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShapeSize() {
        return shapeSize;
    }

    public void setShapeSize(Integer shapeSize) {
        this.shapeSize = shapeSize;
    }

    public double[] getClusterIndicatedTPs() {
        return clusterIndicatedTPs;
    }

    public void setClusterIndicatedTPs(double[] clusterIndicatedTPs) {
        this.clusterIndicatedTPs = clusterIndicatedTPs;
    }

    public double[] getClusterIndicatedSLs() {
        return clusterIndicatedSLs;
    }

    public void setClusterIndicatedSLs(double[] clusterIndicatedSLs) {
        this.clusterIndicatedSLs = clusterIndicatedSLs;
    }

    public double[] getChangesMeanWinRateMomentum() {
        return changesMeanWinRateMomentum;
    }

    public void setChangesMeanWinRateMomentum(double[] changesMeanWinRateMomentum) {
        this.changesMeanWinRateMomentum = changesMeanWinRateMomentum;
    }

    public double[] getChangesMeanReturnMomentum() {
        return changesMeanReturnMomentum;
    }

    public void setChangesMeanReturnMomentum(double[] changesMeanReturnMomentum) {
        this.changesMeanReturnMomentum = changesMeanReturnMomentum;
    }

    public double[] getClusterIndicatedTPsMomentum() {
        return clusterIndicatedTPsMomentum;
    }

    public void setClusterIndicatedTPsMomentum(double[] clusterIndicatedTPsMomentum) {
        this.clusterIndicatedTPsMomentum = clusterIndicatedTPsMomentum;
    }

    public double[] getClusterIndicatedSLsMomentum() {
        return clusterIndicatedSLsMomentum;
    }

    public void setClusterIndicatedSLsMomentum(double[] clusterIndicatedSLsMomentum) {
        this.clusterIndicatedSLsMomentum = clusterIndicatedSLsMomentum;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public int getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(int timeframe) {
        this.timeframe = timeframe;
    }

    public double[] getVolumeCentroid() {
        return volumeCentroid;
    }

    public void setVolumeCentroid(double[] volumeCentroid) {
        this.volumeCentroid = volumeCentroid;
    }

    public Integer getStatsSizeFilter() {
        return statsSizeFilter;
    }

    public void setStatsSizeFilter(Integer statsSizeFilter) {
        this.statsSizeFilter = statsSizeFilter;
    }

    public Integer getDiscountedCoins() {
        return discountedCoins;
    }

    public void setDiscountedCoins(Integer discountedCoins) {
        this.discountedCoins = discountedCoins;
    }

    public Integer getSizeDenominator() {
        return sizeDenominator;
    }

    public void setSizeDenominator(Integer sizeDenominator) {
        this.sizeDenominator = sizeDenominator;
    }

    public Integer getnCoins() {
        return nCoins;
    }

    public void setnCoins(Integer nCoins) {
        this.nCoins = nCoins;
    }
}
