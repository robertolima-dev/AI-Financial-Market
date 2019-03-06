package com.crypfy.core.strategies.vol.strategies;

import com.crypfy.core.entity.Asset;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LessVolAssets {

    public List<Asset> get(List<Asset> assets,int wantedAssets){
        //instances
        List<Asset> lessVolAssets = new ArrayList<Asset>();
        List<Double> values = new ArrayList<>();
        //fill
        for(Asset asset : assets){
            DescriptiveStatistics stats = new DescriptiveStatistics();
            DescriptiveStatistics stats2 = new DescriptiveStatistics();
            if(asset.getAttributes().size() == 5) {
                //stdev attr
                for (int i=0; i<4;i++) values.add( Math.log( asset.getAttributes().get(i+1)/asset.getAttributes().get(i) ) );
                for (Double value : values) stats.addValue(value);
                asset.setWeightMetric(stats.getStandardDeviation());
            }else asset.setWeightMetric(999.0);
        }

        Collections.sort(assets, new Comparator<Asset>() {
            @Override
            public int compare(Asset c1, Asset c2) {
                return Double.compare(c1.getWeightMetric(), c2.getWeightMetric());
            }
        });

        //needed assets
        for(int nAssets=0;nAssets<wantedAssets;nAssets++){
            Asset asset = assets.get(nAssets);
            lessVolAssets.add(asset);
        }

        return lessVolAssets;
    }

    public List<Asset> setWeights(List<Asset> assets){
        //instances
        double constant =0;
        //get w constant
        for (Asset asset : assets) constant += 1/asset.getWeightMetric();
        constant = 1/constant;
        //set weight
        for (Asset asset : assets) asset.setWeight( constant/asset.getWeightMetric() );

        return assets;
    }

    public List<Asset> getRangedVol(List<Asset> assets,int wantedAssets,double lowerLimit,double higherLimit){
        //instances
        List<Asset> lessVolAssets = new ArrayList<Asset>();
        List<Double> values = new ArrayList<>();
        //fill
        for(Asset asset : assets){
            DescriptiveStatistics stats = new DescriptiveStatistics();
            DescriptiveStatistics stats2 = new DescriptiveStatistics();
            if(asset.getAttributes().size() == 5) {
                //stdev attr
                for (int i=0; i<3;i++) values.add( Math.log( asset.getAttributes().get(i+1)/asset.getAttributes().get(i) ) );
                for (Double value : values) stats.addValue(value);
                asset.setWeightMetric(stats.getStandardDeviation());
            }else asset.setWeightMetric(999.0);
        }

        Collections.sort(assets, new Comparator<Asset>() {
            @Override
            public int compare(Asset c1, Asset c2) {
                return Double.compare(c1.getWeightMetric(), c2.getWeightMetric());
            }
        });
        int inputedAssets = 0;
        //needed assets
        for(int nAssets=0;nAssets<assets.size();nAssets++){
            Asset asset = assets.get(nAssets);
            if(lessVolAssets.size()<wantedAssets && asset.getWeightMetric()>lowerLimit && asset.getWeightMetric()<higherLimit) {
                lessVolAssets.add(asset);
                inputedAssets++;
            }
        }

        return inputedAssets>0 ? lessVolAssets : null;
    }

    public double getVol(List<Asset> assets){
        //instances
        List<Double> values = new ArrayList<>();
        double vol=0;
        //fill
        for(Asset asset : assets){
            DescriptiveStatistics stats = new DescriptiveStatistics();
            DescriptiveStatistics stats2 = new DescriptiveStatistics();
            if(asset.getAttributes().size() == 5) {
                //stdev attr
                double max=0,min=999999;
                for(double atr : asset.getAttributes()){
                    if(atr>max) max=atr;
                    if(atr<min) min=atr;
                }
                for (int i=0; i<5;i++) values.add((0 + (((asset.getAttributes().get(i) - min) * 1) / (max - min)) ) );
                for (Double value : values) stats.addValue(value);
                vol += stats.getStandardDeviation();
            }else asset.setWeightMetric(999.0);
        }


        return vol/5;
    }
}
