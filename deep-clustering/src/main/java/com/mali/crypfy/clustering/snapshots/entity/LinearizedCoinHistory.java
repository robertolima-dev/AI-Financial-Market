package com.mali.crypfy.clustering.snapshots.entity;

import java.util.Date;

public class LinearizedCoinHistory {

    private Long idcoinHistory;
    private String idcoin;
    private Date date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private double marketcap;
    private Date created;

    public Long getIdcoinHistory() {
        return idcoinHistory;
    }

    public void setIdcoinHistory(Long idcoinHistory) {
        this.idcoinHistory = idcoinHistory;
    }

    public String getIdcoin() {
        return idcoin;
    }

    public void setIdcoin(String idcoin) {
        this.idcoin = idcoin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(double marketcap) {
        this.marketcap = marketcap;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
