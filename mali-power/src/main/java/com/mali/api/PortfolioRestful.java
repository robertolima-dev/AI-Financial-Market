package com.mali.api;

import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.core.portfolioCalculations.api.PortfolioCalculations;
import com.mali.persistence.entity.Portfolio;
import com.mali.persistence.repository.PortfolioRepository;
import com.mali.portfolio.api.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortfolioRestful {

    @Autowired
    PortfolioService portfolioService;
    @Autowired
    private PortfolioCalculations portfolioCalculations;
    @Autowired
    private PortfolioRepository portfolioRepository;

    @RequestMapping("/portfolios")
    public List<Portfolio> listAll(){
        return portfolioService.getAll();
    }

    @RequestMapping("/portfolios/current")
    public Portfolio getCurrentPortfolio(){
        return portfolioService.currentPortfolio();
    }

    @GetMapping(path = "/portfolios/generate/{portfolioName}/{type}")
    public Portfolio getAndSaveCurrentPortfolio(@PathVariable("portfolioName") String portfolioName, @PathVariable("type") String type) throws CoinDataAPIException {
        Portfolio portfolio = new Portfolio();
        //check type
        if(type.equals("blockchain-cap")){
            portfolio = portfolioCalculations.calculateBlockchainPortfolioCap(10,portfolioName);
            //save
            portfolioRepository.save(portfolio);
            return portfolio;
        }
        if(type.equals("blockchain-vol")){
            portfolio = portfolioCalculations.calculateBlockchainPortfolioVol(10,portfolioName);
            //save
            portfolioRepository.save(portfolio);
            return portfolio;
        }
        if(type.equals("power-portfolio")){
            portfolio = portfolioCalculations.calculatePowerPortfolio(portfolioName);
            //save
            portfolioRepository.save(portfolio);
            return portfolio;
        }
        return null;
    }

}
