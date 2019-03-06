package com.crypfy.core.backtest;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsGenerator {

    public double periodMeanReturn(List<Double> indexes){

        int nIndexex = indexes.size();
        double mean = 0;

        for(int i=0;i<nIndexex-1;i++){
            mean += (indexes.get(i+1)-indexes.get(i))/indexes.get(i);
        }

        return (mean/nIndexex)*100;
    }

    public double totalReturn(List<Double> indexes){

        return ((indexes.get(indexes.size()-1) - 1)/1)*100;
    }

    public double maxDD(List<Double> indexes){
        int nIndexex = indexes.size();
        double dd = 0,max=1;

        for(int i=0;i<nIndexex;i++){
            if(indexes.get(i) > max) max = indexes.get(i);
            if( ((max-indexes.get(i))/max) > dd ) {
                dd = ((max-indexes.get(i))/max);
            }
        }

        return dd*100;
    }

    public double monthlyMeanReturn(List<Double> indexes, int period){

        double mean=0;
        int nIndexes = indexes.size(),months=0;
        List<Double> monthValues = new ArrayList<Double>();

        //weak
        if(period==1){
            for(int i=0;i<nIndexes-4;i++){
                if( (i+1)%4 == 0 || i==0){
                    mean += (indexes.get(i+4) - indexes.get(i))/indexes.get(i);
                    months++;
                }
            }
            return (mean/months)*100;
        }

        //biweak
        if(period==2){
            for(int i=0;i<nIndexes-2;i++){
                if( (i+1)%2 == 0 || i==0){
                    mean += (indexes.get(i+2) - indexes.get(i))/indexes.get(i);
                    months++;
                }
            }
            return (mean/months)*100;
        }

        //monthly
        if(period==4) return periodMeanReturn(indexes);

        return 0;
    }

    public List<Double> indexToPercent(List<Double> indexes){
        List<Double> percents = new ArrayList<Double>();

        for(int i=0; i<indexes.size()-1;i++){
            if(i==0){
                percents.add( ((indexes.get(i)-1)/1)*100 );
            }
            percents.add( ((indexes.get(i+1)-indexes.get(i))/indexes.get(i))*100 );
        }

        return percents;
    }

    public double getSD(List<Double> values){
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for(int i=0 ; i<values.size()-1;i++) stats.addValue( Math.log( values.get(i+1)/values.get(i) ) );
        return stats.getStandardDeviation();
    }

}
