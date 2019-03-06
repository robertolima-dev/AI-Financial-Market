package com.crypfy.core.backtest;

import com.crypfy.core.entity.Asset;
import com.crypfy.core.entity.BacktestResult;
import com.crypfy.core.entity.SnapshotTick;
import com.crypfy.core.entity.StrategyAssetsDistribution;
import com.crypfy.core.enumerations.Strategies;
import com.crypfy.core.enumerations.StrategyPeriod;
import com.crypfy.core.factories.StrategyFactory;
import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import com.crypfy.utils.PeriodConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StrategyBacktest {

    @Autowired
    TickCollector tickCollector;
    @Autowired
    PeriodConverter periodConverter;
    @Autowired
    StrategyFactory strategy;
    @Autowired
    StatisticsGenerator statisticsGenerator;

    public BacktestResult execute(StrategyPeriod period,String strategyName,int nAssets,int nWeaks,double lowerLimit,double higherLimit) throws BacktestException {

        //instances
        BacktestResult backtestResult = new BacktestResult();
        List<SnapshotTick> ticks = tickCollector.getTicks(nWeaks);
        List<Double> indexUpdates = new ArrayList<Double>();
        List<Double> volUpdates = new ArrayList<Double>();
        StrategyAssetsDistribution distribution = new StrategyAssetsDistribution();
        int periodMult = periodConverter.periodToInt(period),currentTick=0,totalTicks=ticks.size(),lastPortfolioUpdateTick=0,ticksUpdated=1;
        boolean started = false,vStarted = false;
        double index = 1,maxIndexValue=1,maxdd=0;

        //tick by tick
        for(currentTick=4;currentTick<totalTicks;currentTick++){
            SnapshotTick tick = ticks.get(currentTick);
            //
            HistoricalCoinSnapshot historicalCoinSnapshot = new HistoricalCoinSnapshot();
            historicalCoinSnapshot.setName("BRL");
            historicalCoinSnapshot.setPrice(1);
            tick.getSnapshots().add(historicalCoinSnapshot);
            //check for index update
            if( ((currentTick-lastPortfolioUpdateTick)>=periodMult && (currentTick-lastPortfolioUpdateTick)%periodMult==0 && started) || currentTick==totalTicks-1){
                //update the index
                double newIndex=0;
                for(Asset asset : distribution.getAssets()){
                    double currentAssetValue=0;
                    boolean hasAsset = false;
                    for(HistoricalCoinSnapshot snapshot : tick.getSnapshots()){
                        if(snapshot.getName().equals(asset.getName())){
                            currentAssetValue = snapshot.getPrice();
                            hasAsset = true;
                        }
                    }
                    if(hasAsset) newIndex += asset.getQuantity()*currentAssetValue;
                    if(!hasAsset) newIndex += asset.getQuantity()*0;
                }
                index = newIndex;
                indexUpdates.add(index);
                started = false;
            }
            //update dd
            if(vStarted){
                //update maxdd
                double newIndex=0;
                for(Asset asset : distribution.getAssets()){
                    double currentAssetValue=0;
                    boolean hasAsset = false;
                    for(HistoricalCoinSnapshot snapshot : tick.getSnapshots()){
                        if(snapshot.getName().equals(asset.getName())){
                            currentAssetValue = snapshot.getPrice();
                            hasAsset = true;
                        }
                    }
                    if(hasAsset) newIndex += asset.getQuantity()*currentAssetValue;
                    if(!hasAsset) newIndex += asset.getQuantity()*0;
                }
                if(newIndex>maxIndexValue) maxIndexValue = newIndex;
                if( (maxIndexValue-newIndex)/maxIndexValue >maxdd ) maxdd = (maxIndexValue-newIndex)/maxIndexValue;
            }
            //certify that theres enough history
            if(ticksUpdated>=periodMult && ticksUpdated%periodMult==0){
                //get portfolio
                //fill the ticks to the strategy
                List<SnapshotTick> sTicks = new ArrayList<SnapshotTick>();
                sTicks.add(tick);
                sTicks.add(ticks.get(currentTick-1));
                sTicks.add(ticks.get(currentTick-2));
                sTicks.add(ticks.get(currentTick-3));
                sTicks.add(ticks.get(currentTick-4));
                distribution = strategy.getService(Strategies.valueOf(strategyName.toUpperCase())).getDistribution(sTicks,nAssets,lowerLimit,higherLimit);
                //distribute the index
                for(Asset asset : distribution.getAssets()){
                    asset.setQuantity( (asset.getWeight()*index*0.995)/asset.getPrice() );
                }
                //portfolio updated
                lastPortfolioUpdateTick=currentTick;
                started = true;
                vStarted = true;
                if(!strategyName.equals("cap")) volUpdates.add(distribution.getVolValue());
            }
            ticksUpdated++;
        }
        //generate final statistics
        backtestResult.setPeriod(period);
        backtestResult.setStrategyName(strategyName);
        backtestResult.setPeriodReturns(indexUpdates);
        backtestResult.setMaxdd(maxdd);
        backtestResult.setMonthlyMeanReturn(statisticsGenerator.monthlyMeanReturn(indexUpdates,periodMult));
        backtestResult.setPeriodMeanReturn(statisticsGenerator.periodMeanReturn(indexUpdates));
        backtestResult.setTotalReturn(statisticsGenerator.totalReturn(indexUpdates));
        backtestResult.setnPeriods(indexUpdates.size());
        backtestResult.setPeriodReturnsPercent(statisticsGenerator.indexToPercent(indexUpdates));
        if(!strategyName.equals("cap")) backtestResult.setVolValues(volUpdates);

        return backtestResult;
    }
}
