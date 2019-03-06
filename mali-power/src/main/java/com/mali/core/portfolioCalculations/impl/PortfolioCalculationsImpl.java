package com.mali.core.portfolioCalculations.impl;

import com.mali.collector.api.CoinData;
import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.collector.json.CoinHistoryJson;
import com.mali.collector.json.CoinJson;
import com.mali.core.portfolioCalculations.api.PortfolioCalculations;
import com.mali.core.rateCalculations.api.RatesCalculator;
import com.mali.persistence.entity.Portfolio;
import com.mali.persistence.entity.PowerRate;
import com.mali.persistence.repository.PortfolioRepository;
import com.mali.persistence.repository.PowerRateRepository;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PortfolioCalculationsImpl implements PortfolioCalculations {

    @Autowired
    PowerRateRepository powerRateRepository;
    @Autowired
    RatesCalculator ratesCalculator;
    @Autowired
    CoinData coinData;

    @Override
    public Portfolio calculateCurrentPortfolio(List<PowerRate> portfolioCoins,String name) {
        //create a new portfolio
        Portfolio portfolio = new Portfolio();
        portfolio.setCoins(portfolioCoins);
        portfolio.setCreated(new Date());
        portfolio.setName(name);
        portfolio.setType("power-portfolio");
        return portfolio;
    }

    @Override
    public Portfolio calculatePowerPortfolio(String portName) throws CoinDataAPIException {
        //get what is needed
        List<PowerRate> coinsWithPowerRate = powerRateRepository.findAll();
        List<String> categorys = uniqueCategorys(coinsWithPowerRate);
        List<PowerRate> portfolioCoins = portfolioCoins(categorys);
        //calculate 30 days SD for each coin
        for(PowerRate powerRate : portfolioCoins){
            powerRate.setWeight(calculateSD(coinData.coinHistoryByIdcoin(powerRate.getIdcoin())));
        }
        List<PowerRate> portfolioCoinsWithWeight = calculateWeight(portfolioCoins);
        Portfolio portfolio = calculateCurrentPortfolio(portfolioCoinsWithWeight,portName);
        return portfolio;
    }

    @Override
    public Portfolio calculateBlockchainPortfolioCap(int nCoinsInPortfolio,String portName) throws CoinDataAPIException {

        //instantiated what is needed
        List<PowerRate> powerRates = new ArrayList<PowerRate>();
        List<CoinJson> coinJsonList= coinData.all(), neededCoins = new ArrayList<CoinJson>();
        int counter=0;
        double sumCap=0;
        //collect the coins
        for(CoinJson coinJson : coinJsonList){
            neededCoins.add(coinJson);
            sumCap += coinJson.getMarketCapUsd().doubleValue();
            counter++;
            if(counter>=nCoinsInPortfolio) break;
        }
        //turn to power rate and set the weights
        for(CoinJson coinJson : neededCoins){
            PowerRate powerRate = new PowerRate();
            powerRate.setIdcoin(coinJson.getIdcoin());
            powerRate.setCategory(coinJson.getCategoryCoin().getName());
            powerRate.setWeight(coinJson.getMarketCapUsd().doubleValue()/sumCap);
            //add it
            powerRates.add(powerRate);
        }
        //now lets set the portfolio
        Portfolio portfolio = new Portfolio();
        portfolio.setCoins(powerRates);
        portfolio.setCreated( new Date());
        portfolio.setName(portName);
        portfolio.setType("blockchain-cap");

        return portfolio;
    }

    @Override
    public Portfolio calculateBlockchainPortfolioVol(int nCoinsInPortfolio, String portName) throws CoinDataAPIException {
        //instantiated what is needed
        List<PowerRate> powerRates = new ArrayList<PowerRate>();
        List<CoinJson> coinJsonList= coinData.all(), neededCoins = new ArrayList<CoinJson>();
        int counter=0;
        //collect the coins
        for(CoinJson coinJson : coinJsonList){
            if( (System.currentTimeMillis() - coinJson.getBirthdate().getTime() ) > 1000*60*60*24*30 ) counter++;
            PowerRate powerRate = new PowerRate();
            powerRate.setWeight(calculateSD(coinData.coinHistoryByIdcoin(coinJson.getIdcoin())));
            powerRate.setCategory(coinJson.getCategoryCoin().getName());
            powerRate.setIdcoin(coinJson.getIdcoin());
            //add
            powerRates.add(powerRate);
            if(counter>=nCoinsInPortfolio) break;
        }
        //correct the weights
        List<PowerRate> correctCoins = calculateWeight(powerRates);
        //now lets set the portfolio
        Portfolio portfolio = new Portfolio();
        portfolio.setCoins(correctCoins);
        portfolio.setCreated( new Date());
        portfolio.setName(portName);
        portfolio.setType("blockchain-vol");

        return portfolio;
    }

    @Override
    public List<String> uniqueCategorys(List<PowerRate> powerRateList) {
        //get all the different category's
        List<String> uniqueCategorys = new ArrayList<String>();
        for(PowerRate powerRate : powerRateList){
            String category = powerRate.getCategory();
            if(!uniqueCategorys.contains(category)) uniqueCategorys.add(category);
        }
        return uniqueCategorys;
    }

    @Override
    public List<PowerRate> portfolioCoins(List<String> categorys) {
        //final list
        List<PowerRate> portfolioCoins = new ArrayList<PowerRate>();
        //get by category
        for(String category : categorys){
            List<PowerRate> powerRates = powerRateRepository.findByCategory(category);
            //if there is some result
            if(powerRates != null) {
                PowerRate selectedPowerRate = new PowerRate();
                double maxRate = 0;
                for (PowerRate powerRate : powerRates) {
                    if (ratesCalculator.finalRate(powerRate) > maxRate) selectedPowerRate = powerRate;
                }
                portfolioCoins.add(selectedPowerRate);
            }
        }
        return portfolioCoins;
    }

    @Override
    public double calculateSD(List<CoinHistoryJson> history) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for(int i=0;i<30;i++){
            stats.addValue(history.get(i).getCloseValue().doubleValue()-history.get(i).getCloseValue().doubleValue());
        }
        return stats.getStandardDeviation();
    }

    @Override
    public List<PowerRate> calculateWeight(List<PowerRate> portfolioCoins) {
        //sum the sd's
        double volatilityFactor=0;
        for(PowerRate powerRate : portfolioCoins) volatilityFactor+= 1/powerRate.getWeight();
        volatilityFactor = 1/volatilityFactor;
        //set the weight
        for(PowerRate powerRate : portfolioCoins) powerRate.setWeight(volatilityFactor/powerRate.getWeight());
        return portfolioCoins;
    }

}
