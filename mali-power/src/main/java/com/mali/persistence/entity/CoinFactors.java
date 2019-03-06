package com.mali.persistence.entity;

import java.util.List;

public class CoinFactors {
    private String idcoin;
    private Double pFactor;
    private String  oFactor;
    private Double wFactor;
    private List<Double> eFactor;
    private Double rFactor;

    public String getIdcoin() {
        return idcoin;
    }

    public void setIdcoin(String idcoin) {
        this.idcoin = idcoin;
    }

    public Double getpFactor() {
        return pFactor;
    }

    public void setpFactor(Double pFactor) {
        this.pFactor = pFactor;
    }

    public String getoFactor() {
        return oFactor;
    }

    public void setoFactor(String oFactor) {
        this.oFactor = oFactor;
    }

    public Double getwFactor() {
        return wFactor;
    }

    public void setwFactor(Double wFactor) {
        this.wFactor = wFactor;
    }

    public List<Double> geteFactor() {
        return eFactor;
    }

    public void seteFactor(List<Double> eFactor) {
        this.eFactor = eFactor;
    }

    public Double getrFactor() {
        return rFactor;
    }

    public void setrFactor(Double rFactor) {
        this.rFactor = rFactor;
    }
}
