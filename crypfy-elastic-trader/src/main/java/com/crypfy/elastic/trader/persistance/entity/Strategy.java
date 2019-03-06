package com.crypfy.elastic.trader.persistance.entity;

import com.crypfy.elastic.trader.persistance.enums.*;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Strategy {

    private String id;
    private List<ExchangerDetails> exchangersDetails;
    private List<SubStrategy> subStrategies;
    private String name;
    private StrategyStatus strategyStatus;
    private StrategyMode mode;
    private BaseCurrency baseCurrency;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ExchangerDetails> getExchangersDetails() {
        return exchangersDetails;
    }

    public void setExchangersDetails(List<ExchangerDetails> exchangersDetails) {
        this.exchangersDetails = exchangersDetails;
    }

    public List<SubStrategy> getSubStrategies() {
        return subStrategies;
    }

    public void setSubStrategies(List<SubStrategy> subStrategies) {
        this.subStrategies = subStrategies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StrategyStatus getStrategyStatus() {
        return strategyStatus;
    }

    public void setStrategyStatus(StrategyStatus strategyStatus) {
        this.strategyStatus = strategyStatus;
    }

    public StrategyMode getMode() {
        return mode;
    }

    public void setMode(StrategyMode mode) {
        this.mode = mode;
    }

    public BaseCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(BaseCurrency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
