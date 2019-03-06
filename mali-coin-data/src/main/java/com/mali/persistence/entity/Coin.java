package com.mali.persistence.entity;

import com.mali.persistence.enumeration.CoinStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@NamedEntityGraph(name = "Coin.detail",
        attributeNodes = @NamedAttributeNode("categoryCoin"))
@Entity
public class Coin implements Serializable {

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

    @Id
    public String getIdcoin() {
        return idcoin;
    }

    public void setIdcoin(String idcoin) {
        this.idcoin = idcoin;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price_usd")
    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    @Column(name = "price_btc")
    public BigDecimal getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(BigDecimal priceBtc) {
        this.priceBtc = priceBtc;
    }

    @Column(name = "volume_24h_usd")
    public BigDecimal getVolume24hUsd() {
        return volume24hUsd;
    }

    public void setVolume24hUsd(BigDecimal volume24hUsd) {
        this.volume24hUsd = volume24hUsd;
    }

    @Column(name = "market_cap_usd")
    public BigDecimal getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(BigDecimal marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    @Column(name = "percent_change_1h")
    public BigDecimal getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(BigDecimal percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    @Column(name = "percent_change_24h")
    public BigDecimal getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(BigDecimal percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    @Column(name = "percent_change_7d")
    public BigDecimal getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(BigDecimal percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    @Column(name = "twitter_followers")
    public BigInteger getTwitterFollowers() {
        return twitterFollowers;
    }

    public void setTwitterFollowers(BigInteger twitterFollowers) {
        this.twitterFollowers = twitterFollowers;
    }

    @Column(name = "birthdate")
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Column(name = "total_active_users")
    public BigInteger getTotalActiveUsers() {
        return totalActiveUsers;
    }

    public void setTotalActiveUsers(BigInteger totalActiveUsers) {
        this.totalActiveUsers = totalActiveUsers;
    }

    @Column(name = "total_transactions_day")
    public BigInteger getTotalTransactionsDay() {
        return totalTransactionsDay;
    }

    public void setTotalTransactionsDay(BigInteger totalTransactionsDay) {
        this.totalTransactionsDay = totalTransactionsDay;
    }

    @Column(name = "last_updated")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public CoinStatus getStatus() {
        return status;
    }

    public void setStatus(CoinStatus status) {
        this.status = status;
    }

    @Column(name = "supply_available")
    public BigDecimal getSupplyAvailable() {
        return supplyAvailable;
    }

    public void setSupplyAvailable(BigDecimal supplyAvailable) {
        this.supplyAvailable = supplyAvailable;
    }

    @Column(name = "supply_total")
    public BigDecimal getSupplyTotal() {
        return supplyTotal;
    }

    public void setSupplyTotal(BigDecimal supplyTotal) {
        this.supplyTotal = supplyTotal;
    }

    @Column(name = "symbol")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcategory_coin")
    public CategoryCoin getCategoryCoin() {
        return categoryCoin;
    }

    public void setCategoryCoin(CategoryCoin categoryCoin) {
        this.categoryCoin = categoryCoin;
    }
}
