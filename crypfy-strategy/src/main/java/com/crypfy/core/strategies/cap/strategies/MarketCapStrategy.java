package com.crypfy.core.strategies.cap.strategies;

import com.crypfy.core.entity.Asset;
import com.crypfy.core.entity.SnapshotTick;
import com.crypfy.core.entity.StrategyAssetsDistribution;
import com.crypfy.core.strategies.api.Strategy;
import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketCapStrategy implements Strategy{


    @Override
    public StrategyAssetsDistribution getDistribution(List<SnapshotTick> ticks, int nAssets,double lowerLimit,double higherLimit) {

        //instances
        StrategyAssetsDistribution assetsDistribution = new StrategyAssetsDistribution();
        List<Asset> assets = new ArrayList<Asset>();
        int accountedAssets = 0;
        double capSum=0;

        //iterate
        for (HistoricalCoinSnapshot snapshot : ticks.get(0).getSnapshots()){
            if(!snapshot.getName().equals("USDT")) {
                Asset asset = new Asset();
                asset.setName(snapshot.getName());
                asset.setPrice(snapshot.getPrice());
                asset.setWeightMetric(snapshot.getCap());
                //sum cap
                capSum += snapshot.getCap();
                //add
                assets.add(asset);
                //enough?
                accountedAssets++;
            }
            if (accountedAssets==nAssets) break;
        }
        //set weights
        for (Asset asset : assets){
            asset.setWeight(asset.getWeightMetric()/capSum);
        }
        //print portfolio
        for(Asset asset : assets){
            System.out.println("Name ="+asset.getName());
            System.out.println("Weight ="+asset.getWeight());
        }
        System.out.println("Date = "+ticks.get(0).getSnapshots().get(0).getDate());
        System.out.println("\n");
        //fulfill
        assetsDistribution.setAssets(assets);
        assetsDistribution.setDate(ticks.get(0).getSnapshots().get(0).getDate());

        return assetsDistribution;
    }
}
