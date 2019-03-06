package com.mali.collector.json;

import com.mali.collector.enumeration.CoinStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class CoinJson {

    private String idcoin;
    private String name;
    private String symbol;
    private BigDecimal supplyAvailable;
    private BigDecimal supplyTotal;
    private BigDecimal priceUsd;
    private BigDecimal priceBtc;
    private BigDecimal volume24hUsd;
    private BigDecimal marketCapUsd;
    private BigDecimal percentChange1h;
    private BigDecimal percentChange24h;
    private BigDecimal percentChange7d;
    private BigInteger twitterFollowers;
    private Date birthdate;
    private BigInteger totalActiveUsers;
    private BigInteger totalTransactionsDay;
    private Date lastUpdated;
    private CoinStatus status;
    private CategoryCoin categoryCoin;

    public CategoryCoin getCategoryCoin() {
        return categoryCoin;
    }

    public void setCategoryCoin(CategoryCoin categoryCoin) {
        this.categoryCoin = categoryCoin;
    }

    public String getIdcoin() {
        return idcoin;
    }

    public void setIdcoin(String idcoin) {
        this.idcoin = idcoin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getSupplyAvailable() {
        return supplyAvailable;
    }

    public void setSupplyAvailable(BigDecimal supplyAvailable) {
        this.supplyAvailable = supplyAvailable;
    }

    public BigDecimal getSupplyTotal() {
        return supplyTotal;
    }

    public void setSupplyTotal(BigDecimal supplyTotal) {
        this.supplyTotal = supplyTotal;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public BigDecimal getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(BigDecimal priceBtc) {
        this.priceBtc = priceBtc;
    }

    public BigDecimal getVolume24hUsd() {
        return volume24hUsd;
    }

    public void setVolume24hUsd(BigDecimal volume24hUsd) {
        this.volume24hUsd = volume24hUsd;
    }

    public BigDecimal getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(BigDecimal marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public BigDecimal getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(BigDecimal percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public BigDecimal getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(BigDecimal percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public BigDecimal getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(BigDecimal percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public BigInteger getTwitterFollowers() {
        return twitterFollowers;
    }

    public void setTwitterFollowers(BigInteger twitterFollowers) {
        this.twitterFollowers = twitterFollowers;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public BigInteger getTotalActiveUsers() {
        return totalActiveUsers;
    }

    public void setTotalActiveUsers(BigInteger totalActiveUsers) {
        this.totalActiveUsers = totalActiveUsers;
    }

    public BigInteger getTotalTransactionsDay() {
        return totalTransactionsDay;
    }

    public void setTotalTransactionsDay(BigInteger totalTransactionsDay) {
        this.totalTransactionsDay = totalTransactionsDay;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public CoinStatus getStatus() {
        return status;
    }

    public void setStatus(CoinStatus status) {
        this.status = status;
    }
}
