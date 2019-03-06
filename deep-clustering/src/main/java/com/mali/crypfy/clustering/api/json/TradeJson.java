package com.mali.crypfy.clustering.api.json;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Exchanger;

public class TradeJson {

    private BigDecimal takeProfit;
    private BigDecimal stopLoss;
    private String orderType;
    private Date expirationDate;
    private String callCoin;
    private BigDecimal callPrice;
    private String timeFrame;
    private String baseCurrency;
    private String intelligenceType;

    public BigDecimal getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(BigDecimal takeProfit) {
        this.takeProfit = takeProfit;
    }

    public BigDecimal getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(BigDecimal stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCallCoin() {
        return callCoin;
    }

    public void setCallCoin(String callCoin) {
        this.callCoin = callCoin;
    }

    public BigDecimal getCallPrice() {
        return callPrice;
    }

    public void setCallPrice(BigDecimal callPrice) {
        this.callPrice = callPrice;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getIntelligenceType() {
        return intelligenceType;
    }

    public void setIntelligenceType(String intelligenceType) {
        this.intelligenceType = intelligenceType;
    }
}
