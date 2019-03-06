package com.mali.collector.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class CoinHistoryJson {

    private Long idcoinHistory;
    private String idcoin;
    private Date date;
    private BigDecimal openValue;
    private BigDecimal highValue;
    private BigDecimal lowValue;
    private BigDecimal closeValue;
    private BigDecimal volume;
    private BigDecimal marketcap;
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

    public BigDecimal getOpenValue() {
        return openValue;
    }

    public void setOpenValue(BigDecimal openValue) {
        this.openValue = openValue;
    }

    public BigDecimal getHighValue() {
        return highValue;
    }

    public void setHighValue(BigDecimal highValue) {
        this.highValue = highValue;
    }

    public BigDecimal getLowValue() {
        return lowValue;
    }

    public void setLowValue(BigDecimal lowValue) {
        this.lowValue = lowValue;
    }

    public BigDecimal getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(BigDecimal closeValue) {
        this.closeValue = closeValue;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(BigDecimal marketcap) {
        this.marketcap = marketcap;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
