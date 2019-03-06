package com.mali.exchanger.communication.impl.bittrex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinDetail {

    private String marketCurrency;
    private String baseCurrency;
    private String marketCurrencyLong;
    private String baseCurrencyLong;
    private double minTradeSize;
    private String marketName;
    private boolean isActive;
    private String created;
    private String notice;
    private String isSponsored;
    private String logoUrl;

    @JsonProperty("MarketCurrency")
    public String getMarketCurrency() {
        return marketCurrency;
    }

    public void setMarketCurrency(String marketCurrency) {
        this.marketCurrency = marketCurrency;
    }
    @JsonProperty("BaseCurrency")
    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
    @JsonProperty("MarketCurrencyLong")
    public String getMarketCurrencyLong() {
        return marketCurrencyLong;
    }

    public void setMarketCurrencyLong(String marketCurrencyLong) {
        this.marketCurrencyLong = marketCurrencyLong;
    }
    @JsonProperty("BaseCurrencyLong")
    public String getBaseCurrencyLong() {
        return baseCurrencyLong;
    }

    public void setBaseCurrencyLong(String baseCurrencyLong) {
        this.baseCurrencyLong = baseCurrencyLong;
    }
    @JsonProperty("MinTradeSize")
    public double getMinTradeSize() {
        return minTradeSize;
    }

    public void setMinTradeSize(double minTradeSize) {
        this.minTradeSize = minTradeSize;
    }
    @JsonProperty("MarketName")
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
    @JsonProperty("IsActive")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    @JsonProperty("Created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    @JsonProperty("Notice")
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
    @JsonProperty("IsSponsored")
    public String getIsSponsored() {
        return isSponsored;
    }

    public void setIsSponsored(String isSponsored) {
        this.isSponsored = isSponsored;
    }
    @JsonProperty("LogoUrl")
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
