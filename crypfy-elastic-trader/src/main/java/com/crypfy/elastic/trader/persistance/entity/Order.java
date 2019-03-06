package com.crypfy.elastic.trader.persistance.entity;

import com.crypfy.elastic.trader.persistance.enums.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

public class Order {

    private String id;
    private OrderStatus orderStatus;
    private OrderCloseReason closeReason;
    private Exchanger exchanger;
    private ExchangerDetails exchangerDetails;
    private String toCoin;
    private String fromCoin;
    private BigDecimal initialBaseAmount;
    private BigDecimal finalBaseAmount;
    private BigDecimal orderAmount;
    private BigDecimal alreadyExec;
    private BigDecimal remainingAmount;
    private BigDecimal firstStepOpenPrice;
    private BigDecimal firstStepClosePrice;
    private BigDecimal secondStepOpenPrice;
    private BigDecimal secondStepClosePrice;
    private BigDecimal takeProfit;
    private BigDecimal stopLoss;
    private OrderType orderType;
    private StrategyMode mode;
    private Date openDate;
    private Date closeDate;
    private Date expirationDate;
    private String exchangerFirstStepBuyOrderId;
    private String exchangerFirstStepSellOrderId;
    private String exchangerSecondStepBuyOrderId;
    private String exchangerSecondStepSellOrderId;
    private String strategyName;
    private String callCoin;
    private BigDecimal callPrice;
    private OrderPriority priority;
    private TimeFrame timeFrame;
    private BaseCurrency baseCurrency;
    private boolean isTwoStep;
    private boolean tpSlNeeded;
    private IntelligenceType intelligenceType;
    private String subStrategyName;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Exchanger getExchanger() {
        return exchanger;
    }

    public void setExchanger(Exchanger exchanger) {
        this.exchanger = exchanger;
    }

    public String getToCoin() {
        return toCoin;
    }

    public void setToCoin(String toCoin) {
        this.toCoin = toCoin;
    }

    public String getFromCoin() {
        return fromCoin;
    }

    public void setFromCoin(String fromCoin) {
        this.fromCoin = fromCoin;
    }

    public BigDecimal getInitialBaseAmount() {
        return initialBaseAmount;
    }

    public void setInitialBaseAmount(BigDecimal initialBaseAmount) {
        this.initialBaseAmount = initialBaseAmount;
    }

    public BigDecimal getFinalBaseAmount() {
        return finalBaseAmount;
    }

    public void setFinalBaseAmount(BigDecimal finalBaseAmount) {
        this.finalBaseAmount = finalBaseAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getFirstStepOpenPrice() {
        return firstStepOpenPrice;
    }

    public void setFirstStepOpenPrice(BigDecimal firstStepOpenPrice) {
        this.firstStepOpenPrice = firstStepOpenPrice;
    }

    public BigDecimal getFirstStepClosePrice() {
        return firstStepClosePrice;
    }

    public void setFirstStepClosePrice(BigDecimal firstStepClosePrice) {
        this.firstStepClosePrice = firstStepClosePrice;
    }

    public BigDecimal getSecondStepOpenPrice() {
        return secondStepOpenPrice;
    }

    public void setSecondStepOpenPrice(BigDecimal secondStepOpenPrice) {
        this.secondStepOpenPrice = secondStepOpenPrice;
    }

    public BigDecimal getSecondStepClosePrice() {
        return secondStepClosePrice;
    }

    public void setSecondStepClosePrice(BigDecimal secondStepClosePrice) {
        this.secondStepClosePrice = secondStepClosePrice;
    }

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

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public StrategyMode getMode() {
        return mode;
    }

    public void setMode(StrategyMode mode) {
        this.mode = mode;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getExchangerFirstStepBuyOrderId() {
        return exchangerFirstStepBuyOrderId;
    }

    public void setExchangerFirstStepBuyOrderId(String exchangerFirstStepBuyOrderId) {
        this.exchangerFirstStepBuyOrderId = exchangerFirstStepBuyOrderId;
    }

    public String getExchangerFirstStepSellOrderId() {
        return exchangerFirstStepSellOrderId;
    }

    public void setExchangerFirstStepSellOrderId(String exchangerFirstStepSellOrderId) {
        this.exchangerFirstStepSellOrderId = exchangerFirstStepSellOrderId;
    }

    public String getExchangerSecondStepBuyOrderId() {
        return exchangerSecondStepBuyOrderId;
    }

    public void setExchangerSecondStepBuyOrderId(String exchangerSecondStepBuyOrderId) {
        this.exchangerSecondStepBuyOrderId = exchangerSecondStepBuyOrderId;
    }

    public String getExchangerSecondStepSellOrderId() {
        return exchangerSecondStepSellOrderId;
    }

    public void setExchangerSecondStepSellOrderId(String exchangerSecondStepSellOrderId) {
        this.exchangerSecondStepSellOrderId = exchangerSecondStepSellOrderId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getCallCoin() {
        return callCoin;
    }

    public void setCallCoin(String callCoin) {
        this.callCoin = callCoin;
    }

    public OrderPriority getPriority() {
        return priority;
    }

    public void setPriority(OrderPriority priority) {
        this.priority = priority;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

    public OrderCloseReason getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(OrderCloseReason closeReason) {
        this.closeReason = closeReason;
    }

    public BigDecimal getCallPrice() {
        return callPrice;
    }

    public void setCallPrice(BigDecimal callPrice) {
        this.callPrice = callPrice;
    }

    public BaseCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(BaseCurrency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public boolean isTwoStep() {
        return isTwoStep;
    }

    public void setTwoStep(boolean twoStep) {
        isTwoStep = twoStep;
    }

    public IntelligenceType getIntelligenceType() {
        return intelligenceType;
    }

    public void setIntelligenceType(IntelligenceType intelligenceType) {
        this.intelligenceType = intelligenceType;
    }

    public String getSubStrategyName() {
        return subStrategyName;
    }

    public void setSubStrategyName(String subStrategyName) {
        this.subStrategyName = subStrategyName;
    }

    public boolean isTpSlNeeded() {
        return tpSlNeeded;
    }

    public void setTpSlNeeded(boolean tpSlNeeded) {
        this.tpSlNeeded = tpSlNeeded;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getAlreadyExec() {
        return alreadyExec;
    }

    public void setAlreadyExec(BigDecimal alreadyExec) {
        this.alreadyExec = alreadyExec;
    }

    public ExchangerDetails getExchangerDetails() {
        return exchangerDetails;
    }

    public void setExchangerDetails(ExchangerDetails exchangerDetails) {
        this.exchangerDetails = exchangerDetails;
    }
}
