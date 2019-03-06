package com.mali.crypfy.clustering.cluster;

import com.mali.crypfy.clustering.cluster.exceptions.ComparativeMetricsException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ComparativeMetrics {

    public double absoluteDistancePercent(double[] firstSet, double[] secondSet) throws ComparativeMetricsException {

        double[] distances = new double[firstSet.length];

        //same size needed
        if(firstSet.length!=secondSet.length) throw new ComparativeMetricsException("Os conjuntos de dados devem ser do mesmo tamanho");

        //iterate through data
        for (int iterator = 0; iterator < firstSet.length; iterator ++){
            distances[iterator] = Math.abs(firstSet[iterator]-secondSet[iterator]);
        }
        //order it
        double[] unchangedDistance = distances;
        Arrays.sort(distances);
        //number of allowed exceptions
        int allowedExceptions = distances.length%6==0 ? (distances.length/6) -1 : 0;
        //it cannot be the last array data
        if (allowedExceptions>0) {
            if (unchangedDistance.length-1 == ArrayUtils.indexOf(unchangedDistance, distances[distances.length-1])) allowedExceptions = 0;
        }
        return distances.length>allowedExceptions+1 ? distances[distances.length-1-allowedExceptions] : distances[distances.length-1];
    }

    public double correlation(double[] firstSet, double[] secondSet){

        return new PearsonsCorrelation().correlation(firstSet,secondSet);
    }

}
