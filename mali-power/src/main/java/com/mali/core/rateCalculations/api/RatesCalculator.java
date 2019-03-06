package com.mali.core.rateCalculations.api;

import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.collector.json.CoinHistoryJson;
import com.mali.collector.json.CoinJson;
import com.mali.persistence.entity.CoinFactors;
import com.mali.persistence.entity.PowerRate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface RatesCalculator {

    //final ratings
    public List<PowerRate> calculateRelevantRates(List<CoinFactors> relevantCoinsFactors);
    public Double finalRate(PowerRate powerRate);

    //concept factors
    public Double       pFactor(double twitterFollowers,int daysOfExistence);
    public String       oFactor(String categoryCoin);
    public Double       wFactor(Double volume24hrsMean, int daysOfExistence);
    public List<Double> eFactor(List<CoinHistoryJson> history);
    public Double       rFactor(Double volume24hrsMean, Double marketCapMean);

    //rates
    public List<PowerRate> calculatePowerRates(BigDecimal minimumCap) throws CoinDataAPIException;
}
