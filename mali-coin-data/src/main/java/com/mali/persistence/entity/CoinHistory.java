package com.mali.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class CoinHistory {
    private Long idcoinHistory;
    private String idcoin;
    private String symbol;
    private Date date;
    private BigDecimal openValue;
    private BigDecimal highValue;
    private BigDecimal lowValue;
    private BigDecimal closeValue;
    private BigDecimal volume;
    private BigDecimal marketcap;
    private Date created;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

    @Column(name = "open_value")
    public BigDecimal getOpenValue() {
        return openValue;
    }

    public void setOpenValue(BigDecimal openValue) {
        this.openValue = openValue;
    }

    @Column(name = "high_value")
    public BigDecimal getHighValue() {
        return highValue;
    }

    public void setHighValue(BigDecimal highValue) {
        this.highValue = highValue;
    }

    @Column(name = "low_value")
    public BigDecimal getLowValue() {
        return lowValue;
    }

    public void setLowValue(BigDecimal lowValue) {
        this.lowValue = lowValue;
    }

    @Column(name = "close_value")
    public BigDecimal getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(BigDecimal closeValue) {
        this.closeValue = closeValue;
    }

    @Column(name = "volume")
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
