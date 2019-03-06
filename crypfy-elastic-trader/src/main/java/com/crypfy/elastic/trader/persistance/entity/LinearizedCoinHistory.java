package com.crypfy.elastic.trader.persistance.entity;

import java.util.Date;

public class LinearizedCoinHistory{

    private Long idcoinHistory;
    private String idcoin;
    private Date date;
    private Integer open;
    private Integer high;
    private Integer low;
    private Integer close;
    private Integer volume;
    private Integer marketcap;
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

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(Integer marketcap) {
        this.marketcap = marketcap;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
