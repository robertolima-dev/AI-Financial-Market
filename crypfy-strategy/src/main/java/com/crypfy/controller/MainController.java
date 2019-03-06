package com.crypfy.controller;

import com.crypfy.core.backtest.BacktestException;
import com.crypfy.core.backtest.StrategyBacktest;
import com.crypfy.core.backtest.TickCollector;
import com.crypfy.core.entity.Asset;
import com.crypfy.core.entity.BacktestResult;
import com.crypfy.core.entity.OptResult;
import com.crypfy.core.entity.StrategyAssetsDistribution;
import com.crypfy.core.enumerations.Strategies;
import com.crypfy.core.enumerations.StrategyPeriod;
import com.crypfy.core.factories.StrategyFactory;
import com.crypfy.crawler.api.CoinDataExtractor;
import com.crypfy.crawler.exception.ReadDataCoinException;
import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import com.crypfy.persistence.repository.HistoricalCoinSnapshotRepository;
import com.crypfy.utils.DateUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    CoinDataExtractor coinDataExtractor;
    @Autowired
    HistoricalCoinSnapshotRepository historicalCoinSnapshotRepository;
    @Autowired
    StrategyBacktest backtest;
    @Autowired
    StrategyFactory strategy;
    @Autowired
    TickCollector tickCollector;

    @GetMapping(path = "/start-data")
    public List<HistoricalCoinSnapshot> get() throws ReadDataCoinException {

        //instantiate
        List<HistoricalCoinSnapshot> historicalCoinSnapshots = new ArrayList<HistoricalCoinSnapshot>();
        DateUtils dateUtils = new DateUtils();
        int weak = 0;

        //iterate through time
        for(DateTime dateTime : dateUtils.getSundays("01/01/2018","04/08/2018")) {

            List<HistoricalCoinSnapshot> snapshots = coinDataExtractor.read(dateUtils.datetimeToStringCoinMarketCap(dateTime),weak);
            //iterate
            for (HistoricalCoinSnapshot snapshot : snapshots) {
                historicalCoinSnapshotRepository.save(snapshot);
                historicalCoinSnapshots.add(snapshot);
            }
            weak++;
        }

        return historicalCoinSnapshots;

    }

    @GetMapping(path = "/backtest/{period}/{stratName}/{nAssets}/{nWeaks}/{min}/{max}")
    public BacktestResult execute(@PathVariable("period") String period,@PathVariable("stratName") String stratName,@PathVariable("nAssets") int nAssets,@PathVariable("nWeaks") int nWeaks,@PathVariable("min") int min,@PathVariable("max") int max){
        try {
            return backtest.execute(StrategyPeriod.valueOf(period.toUpperCase()),stratName,nAssets,nWeaks,min/100.0,max/100.0);
        } catch (BacktestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(path = "/test")
    public Object getTest(){

        Document doc= null;
        try {
            doc = Jsoup.connect("https://www.ccn.com").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getting table
        String element = doc.getElementsByClass("main d-flex flex-column").html();

        return element;

    }

    @GetMapping(path = "/current-portfolio/{stratName}/{nAssets}")
    public StrategyAssetsDistribution executeDistribution(@PathVariable("stratName") String stratName, @PathVariable("nAssets") int nAssets){
        return strategy.getService(Strategies.valueOf(stratName.toUpperCase())).getDistribution(tickCollector.getTicksForCurrentPort(),nAssets,0,1);
    }

    @GetMapping(path = "/opt")
    public List<OptResult> getOpt() throws BacktestException {
        //inst
        List<OptResult> results = new ArrayList<>();

        //iterate through possibilities
        double inferiorLimit=0,superiorLimit=0,pass=0;
        for(inferiorLimit=0.08 ; inferiorLimit<=0.26 ; inferiorLimit += 0.02){
            pass+=2;
            //superior limit
            for(superiorLimit=0.12 ; superiorLimit<=0.34 ; superiorLimit += 0.02){
                //superior > inferior
                if(superiorLimit>inferiorLimit){
                    //make a test and save the result
                    BacktestResult backtestResult = new BacktestResult();
                    backtestResult = backtest.execute(StrategyPeriod.valueOf("MONTHLY"),"RVOL",15,155,inferiorLimit,superiorLimit);
                    OptResult result = new OptResult();
                    result.setInferiorL(inferiorLimit);
                    result.setSuperiorL(superiorLimit);
                    result.setMaxDD(backtestResult.getMaxdd());
                    result.setPeriodMean(backtestResult.getPeriodMeanReturn());
                    result.setTotalReturn(backtestResult.getTotalReturn());
                    result.setComparative(backtestResult.getTotalReturn()/backtestResult.getMaxdd());
                    results.add(result);
                }
            }
            System.out.println("Passo: "+pass+" de um total de 16..");
        }
        //order ir by profit
        Collections.sort(results, new Comparator<OptResult>() {
            @Override
            public int compare(OptResult c1, OptResult c2) {
                return Double.compare(c1.getComparative(), c2.getComparative());
            }
        });

        Collections.reverse(results);

        return  results;
    }


}
