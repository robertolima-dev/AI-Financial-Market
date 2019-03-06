package com.mali.core.portfolioCalculations.api;

import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.collector.json.CoinHistoryJson;
import com.mali.persistence.entity.Portfolio;
import com.mali.persistence.entity.PowerRate;
import java.util.List;

public interface PortfolioCalculations {
    public Portfolio calculateCurrentPortfolio(List<PowerRate> portfolioCoins,String name);
    public Portfolio calculatePowerPortfolio(String portName) throws CoinDataAPIException;
    public Portfolio calculateBlockchainPortfolioCap(int nCoinsInPortfolio,String portName) throws CoinDataAPIException;
    public Portfolio calculateBlockchainPortfolioVol(int nCoinsInPortfolio,String portName) throws CoinDataAPIException;
    public List<String> uniqueCategorys(List<PowerRate> powerRateList);
    public List<PowerRate> portfolioCoins(List<String> categorys);
    public double calculateSD(List<CoinHistoryJson> history);
    public List<PowerRate> calculateWeight(List<PowerRate> portfolioCoins);
}
