package com.mali.core.rateCalculations.impl;

import com.mali.collector.api.CoinData;
import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.collector.json.CoinHistoryJson;
import com.mali.collector.json.CoinJson;
import com.mali.core.rateCalculations.api.RatesCalculator;
import com.mali.persistence.entity.CoinFactors;
import com.mali.persistence.entity.PowerRate;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RatesCalculatorImpl implements RatesCalculator {

    @Autowired
    CoinData coinData;

    @Override
    public List<PowerRate> calculateRelevantRates(List<CoinFactors> relevantCoinsFactors) {
        //manage all the variables
        List<PowerRate> powerRates = new ArrayList<PowerRate>();
        //maps
        Map<String, Double> pFactors = new HashMap<String, Double>();
        Map<String, String> oFactors = new HashMap<String, String>();
        Map<String, Double> wFactors = new HashMap<String, Double>();
        Map<String, List<Double>> eFactors = new HashMap<String, List<Double>>();
        Map<String, Double> rFactors = new HashMap<String, Double>();
        //lists of data
        DescriptiveStatistics pStat = new DescriptiveStatistics();
        Frequency oStat = new Frequency();
        DescriptiveStatistics wStat = new DescriptiveStatistics();
        DescriptiveStatistics eStat = new DescriptiveStatistics();
        DescriptiveStatistics eStat2 = new DescriptiveStatistics();
        DescriptiveStatistics rStat = new DescriptiveStatistics();
        //fill them
        for (int i = 0; i < relevantCoinsFactors.size(); i++) {
            //p
            pFactors.put(relevantCoinsFactors.get(i).getIdcoin(), relevantCoinsFactors.get(i).getpFactor());
            pStat.addValue(relevantCoinsFactors.get(i).getpFactor());
            //o
            oFactors.put(relevantCoinsFactors.get(i).getIdcoin(), relevantCoinsFactors.get(i).getoFactor());
            oStat.addValue(relevantCoinsFactors.get(i).getoFactor());
            //w
            wFactors.put(relevantCoinsFactors.get(i).getIdcoin(), relevantCoinsFactors.get(i).getwFactor());
            wStat.addValue(relevantCoinsFactors.get(i).getwFactor());
            //e
            eFactors.put(relevantCoinsFactors.get(i).getIdcoin(), relevantCoinsFactors.get(i).geteFactor());
            eStat.addValue(relevantCoinsFactors.get(i).geteFactor().get(0));
            eStat2.addValue(relevantCoinsFactors.get(i).geteFactor().get(1));
            //r
            rFactors.put(relevantCoinsFactors.get(i).getIdcoin(), relevantCoinsFactors.get(i).getrFactor());
            rStat.addValue(relevantCoinsFactors.get(i).getrFactor());
        }
        //do some basic math
        //needed variables
        double pMean = pStat.getMean(), pSD = pStat.getStandardDeviation(), wMean = wStat.getMean(), wSD = wStat.getStandardDeviation(),
                e1Mean = eStat.getMean(), e2Mean = eStat2.getMean(), e1SD = eStat.getStandardDeviation(), e2SD = eStat2.getStandardDeviation(),
                rMean = rStat.getMean(), rSD = rStat.getStandardDeviation();
        //put things on place
        for (int j = 0; j < relevantCoinsFactors.size(); j++) {
            String coin = relevantCoinsFactors.get(j).getIdcoin();
            //lets get the rates
            PowerRate powerRate = new PowerRate();
            powerRate.setIdcoin(coin);
            //p rate
            double pRate = 3 + ((pFactors.get(coin) - pMean) / pSD);
            if (pRate > 5) pRate = 5;
            powerRate.setpRate(pRate);
            //o rate
            double oFactor=0,oRate=0;
            if(oStat.getCount(oFactors.get(coin))==0) oFactor=0;
            if(oStat.getCount(oFactors.get(coin))==1) oFactor=3;
            if(oStat.getCount(oFactors.get(coin))>1)  oFactor= 1/Math.log(oStat.getCount(oFactors.get(coin)));
            oRate = 2 + oFactor;
            powerRate.setoRate(oRate);
            powerRate.setCategory(oFactors.get(coin));
            //w rate
            double wRate = 3 + ((wFactors.get(coin) - wMean) / wSD);
            if (wRate > 5) wRate = 5;
            powerRate.setwRate(wRate);
            //e rate
            double e1Rate = 3 + ((eFactors.get(coin).get(0) - e1Mean) / e1SD), e2Rate = 3 + ((eFactors.get(coin).get(1) - e2Mean) / e2SD), eRate;
            if (e1Rate > 5) e1Rate = 5;
            if (e2Rate > 5) e2Rate = 5;
            eRate = (e1Rate + e2Rate) / 2;
            powerRate.seteRate(eRate);
            //w rate
            double rRate = 3 + ((rFactors.get(coin) - rMean) / rSD);
            if (rRate > 5) rRate = 5;
            powerRate.setwRate(rRate);
            //add it
            powerRates.add(powerRate);
        }

        return powerRates;
    }

    @Override
    public Double finalRate(PowerRate powerRate) {
        return (powerRate.getpRate()+powerRate.getoRate()+powerRate.getwRate()+powerRate.geteRate()+powerRate.getrRate())/5;
    }

    @Override
    public Double pFactor(double twitterFollowers, int daysOfExistence) {
        return twitterFollowers / daysOfExistence;
    }

    @Override
    public String oFactor(String categoryCoin) {
        return categoryCoin;
    }

    @Override
    public Double wFactor(Double volume24hrsMean, int daysOfExistence) {
        return volume24hrsMean / daysOfExistence;
    }

    @Override
    public List<Double> eFactor(List<CoinHistoryJson> history) {
        //needed variables
        List<Double> growthAndMaxDD = new ArrayList<Double>();
        int historySize = history.size(), maxPosition = 0, minPosition = 0;
        if(historySize>365) historySize=365;
        double max = 0, min = 999999999,growthFactor=0,maxDD=0;
        //iterate
        for (int i = 0; i < historySize; i++) {
            //maximum
            if (history.get(i).getHighValue().doubleValue() > max) {
                max = history.get(i).getHighValue().doubleValue();
                maxPosition = i;
            }
            //minimum
            if (history.get(i).getLowValue().doubleValue() < min) {
                min = history.get(i).getLowValue().doubleValue();
                minPosition = i;
            }
        }
        //do some math to get the maxDD and growthFactor
        if(maxPosition>minPosition){
            growthFactor = (max-min)/min;
            double localMax=0;
            for (int i = 0; i < minPosition; i++){
                if (history.get(i).getHighValue().doubleValue() > localMax) localMax = history.get(i).getHighValue().doubleValue();
            }
            maxDD = (localMax-min)/localMax;
        }else{
            maxDD = (max-min)/max;
            double localMin=999999999;
            for (int i = 0; i < maxPosition; i++){
                if (history.get(i).getLowValue().doubleValue() < localMin) localMin = history.get(i).getLowValue().doubleValue();
            }
            growthFactor = (max-localMin)/localMin;
        }
        growthAndMaxDD.add(growthFactor);
        growthAndMaxDD.add(maxDD);
        return growthAndMaxDD;
    }

    @Override
    public Double rFactor(Double volume24hrsMean, Double marketCapMean) {
        return volume24hrsMean / marketCapMean;
    }

    @Override
    public List<PowerRate> calculatePowerRates(BigDecimal minimumCap) throws CoinDataAPIException {
        //instances
        //all coins with cap>10kk (and verified???)
        List<CoinJson> coins = coinData.coinByMarketCap(minimumCap);
        List<CoinFactors> coinFactors = new ArrayList<CoinFactors>();
        List<PowerRate> powerRates = new ArrayList<PowerRate>();
        //get the factors for each coin
        for(CoinJson coin : coins){
            int daysOfExistence = (int) ( System.currentTimeMillis() - coin.getBirthdate().getTime() )/(1000*60*24);
            double volume24hrsMean=0,marketCapMean=0;
            CoinFactors coinFactor = new CoinFactors();
            List<CoinHistoryJson> coinHistory = coinData.coinHistoryByIdcoin(coin.getIdcoin());
            //get the means
            for(int i=0;i<15;i++){
                volume24hrsMean += coinHistory.get(i).getVolume().doubleValue();
                marketCapMean += coinHistory.get(i).getMarketcap().doubleValue();
            }
            volume24hrsMean=volume24hrsMean/15;
            marketCapMean=marketCapMean/15;
            //set the factors
            coinFactor.setpFactor(pFactor(coin.getTwitterFollowers().doubleValue(),daysOfExistence));
            coinFactor.setoFactor(oFactor(coin.getCategoryCoin().getName()));
            coinFactor.setwFactor(wFactor(volume24hrsMean,daysOfExistence));
            coinFactor.seteFactor(eFactor(coinHistory));
            coinFactor.setrFactor(rFactor(volume24hrsMean,marketCapMean));
            coinFactor.setIdcoin(coin.getIdcoin());
            coinFactors.add(coinFactor);
        }
        //get the rates
        powerRates = calculateRelevantRates(coinFactors);
        return powerRates;
    }
}
