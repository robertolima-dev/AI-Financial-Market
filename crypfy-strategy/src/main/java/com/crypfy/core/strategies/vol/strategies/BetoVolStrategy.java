package com.crypfy.core.strategies.vol.strategies;

import com.crypfy.core.entity.Asset;
import com.crypfy.core.entity.SnapshotTick;
import com.crypfy.core.entity.StrategyAssetsDistribution;
import com.crypfy.core.strategies.api.Strategy;
import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BetoVolStrategy implements Strategy {

    @Autowired
    LessVolAssets lessVolAssets;

    @Override
    public StrategyAssetsDistribution getDistribution(List<SnapshotTick> ticks, int nAssets,double lowerLimit,double higherLimit) {

        //instances
        StrategyAssetsDistribution assetsDistribution = new StrategyAssetsDistribution();
        List<Asset> assets = new ArrayList<Asset>();

        //iterate ticks
        for(int currentTick=1;currentTick<6;currentTick++){
            SnapshotTick tick = ticks.get(currentTick);
            int snapSize = tick.getSnapshots().size();
            boolean assetFound = false;
            //iterate coin snapshots
            if(currentTick==1) {
                for (int currentSnap = 0; currentSnap < snapSize; currentSnap++) {
                    HistoricalCoinSnapshot snapshot = tick.getSnapshots().get(currentSnap);

                    boolean hasIt = false;

                    for(HistoricalCoinSnapshot coinSnapshot : ticks.get(0).getSnapshots()){
                        if (coinSnapshot.getName().equals(snapshot.getName())) {
                            hasIt = true;
                        }
                    }

                    //select the candidates
                    if (!snapshot.getName().equals("USDT") && assets.size() < 20 && hasIt) {
                        List<Double> attr = new ArrayList<Double>();
                        Asset asset = new Asset();
                        asset.setName(snapshot.getName());
                        //find correct price
                        double price=0;
                        for(HistoricalCoinSnapshot coinSnapshot : ticks.get(0).getSnapshots()){
                            if (coinSnapshot.getName().equals(asset.getName())) {
                                price = coinSnapshot.getPrice();
                            }
                        }
                        asset.setPrice(price);
                        attr.add(snapshot.getPrice());
                        asset.setAttributes(attr);
                        //add
                        assets.add(asset);
                    }
                }
            }
            //collect attributes
            if(currentTick>1){
                //find attr for each asset
                for(Asset asset : assets){
                    //found it?
                    for(int currentSnap=0;currentSnap<snapSize;currentSnap++) {
                        HistoricalCoinSnapshot snapshot = tick.getSnapshots().get(currentSnap);
                        if (snapshot.getName().equals(asset.getName())) {
                            asset.getAttributes().add(snapshot.getPrice());
                            assetFound = true;
                        }
                    }
                    if(!assetFound) asset.getAttributes().add(999999.0);
                }
            }
        }
        //treat assets
        assets = lessVolAssets.get(assets,nAssets);
        assets = lessVolAssets.setWeights(assets);
        //supress vol
        //vol number
        double vol=0;
        for(Asset asset : assets){
            vol+= asset.getWeightMetric();
        }
        vol=vol/assets.size();
        ///compare this volatility whith the actual one
        if(vol<0.38){
            assets = new ArrayList<Asset>();
            Asset asset = new Asset();
            asset.setWeight(1);
            asset.setName("BRL");
            asset.setPrice(1);
            //add
            assets.add(asset);
        }
        //print portfolio
        for(Asset asset : assets){
            System.out.println("Name ="+asset.getName());
            System.out.println("Weight ="+asset.getWeight());
        }
        System.out.println("Date = "+ticks.get(0).getSnapshots().get(0).getDate());
        System.out.println("\n");
        //set distribution
        assetsDistribution.setAssets(assets);
        assetsDistribution.setDate(ticks.get(0).getSnapshots().get(0).getDate());
        assetsDistribution.setVolValue(vol);

        return assetsDistribution;
    }
}
