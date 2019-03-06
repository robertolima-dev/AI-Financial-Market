package com.mali.coin.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CoinMarketCapTicker {

    private String id;
    private String name;
    private String symbol;
    private String rank;
    private String priceUsd;
    private String priceBtc;
    private String VolumeUsd24h;
    private String marketCapUsd;
    private String availableSupply;
    private String totalSupply;
    private String percentChange1h;
    private String percentChange24h;
    private String percentChange7d;
    private Date lastUpated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @JsonProperty("price_usd")
    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    @JsonProperty("price_btc")
    public String getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(String priceBtc) {
        this.priceBtc = priceBtc;
    }

    @JsonProperty("24h_volume_usd")
    public String getVolumeUsd24h() {
        return VolumeUsd24h;
    }

    public void setVolumeUsd24h(String volumeUsd24h) {
        VolumeUsd24h = volumeUsd24h;
    }

    @JsonProperty("available_supply")
    public String getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    @JsonProperty("total_supply")
    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    @JsonProperty("percent_change_1h")
    public String getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    @JsonProperty("percent_change_24h")
    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    @JsonProperty("percent_change_7d")
    public String getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    @JsonProperty("last_updated")
    public Date getLastUpated() {
        return lastUpated;
    }

    public void setLastUpated(Date lastUpated) {
        this.lastUpated = lastUpated;
    }

    @JsonProperty("market_cap_usd")
    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }
}
