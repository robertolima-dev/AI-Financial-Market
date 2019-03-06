package com.mali.crypfy.clustering.cluster;

import com.mali.crypfy.clustering.cluster.exceptions.ComparativeMetricsException;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.snapshots.entity.SnapshotData;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class Clustering {

    public List<Cluster> hierarchicalClustering(List<SnapshotData> dataToCluster, double clusterSimilarity,int sizeDenominator){

        //set the list for the clusters
        List<Cluster> clusters = new ArrayList<>();
        int numberData = dataToCluster.size();

        //iterate through the data
        for (int iterator = 1; iterator < numberData; iterator ++) {

            //data to be compared
            SnapshotData firstData = dataToCluster.get(0), data = dataToCluster.get(iterator);

            //there is any cluster?
            if (iterator == 1){
                //they belong to the same cluster?
                if (isSameCluster(firstData.getShape(),data.getShape(),clusterSimilarity) && isSameCluster(firstData.getVolumeShape(),data.getVolumeShape(),clusterSimilarity)){
                    //make one cluster for the two data's
                    clusters.add(generateNewCluster(firstData,clusterSimilarity));
                    clusters.get(0).getPoints().add(data);
                    System.out.println("\nThe first cluster were defined. The first two data, belongs to the same cluster!");
                } else {
                    //one new cluster for each data
                    clusters.add(generateNewCluster(data,clusterSimilarity));
                    clusters.add(generateNewCluster(firstData,clusterSimilarity));
                    System.out.println("The first two clusters were defined. The first two data belong to different clusters!");
                }
            } else {
                //check if the data fit in some cluster
                boolean isFitted = false;
                for (Cluster cluster : clusters) {
                    //it fits?
                    if (isSameCluster(data.getShape(), cluster.getCentroid(),clusterSimilarity) && isSameCluster(data.getVolumeShape(), cluster.getVolumeCentroid(),clusterSimilarity)) {
                        cluster.getPoints().add(data);
                        isFitted = true;
                    }
                    //set new interation
                    cluster.setClusterInterations(cluster.getClusterInterations()+1);
                    if (isFitted) break;
                }
                //didn't fitted?
                if (!isFitted) {
                    //add a new cluster then
                    clusters.add(generateNewCluster(data,clusterSimilarity));
                }
            }

            if (iterator%((int) numberData/50)==0){
                removeClusterNoise(clusters,numberData/50,sizeDenominator);
                double percent = Double.valueOf(iterator)/numberData;
                BigDecimal changePercent = new BigDecimal(100*percent).setScale(2, RoundingMode.DOWN);
                System.out.println("O número atual de clusters é: "+clusters.size()+". Clusterização em : "+changePercent+"%...");
            }
        }
        //remove final noise
        removeClusterNoise(clusters,0,sizeDenominator);
        System.out.println("O número final de clusters é: "+clusters.size()+". Clusterização 100%");
        return clusters;
    }

    public boolean isSameCluster(double[] firstDataSet,double[] secondDataSet, double similarity){
        //current way of comparing data
        ComparativeMetrics compare = new ComparativeMetrics();
        double distance = 0;
        boolean sameCluster = false;
        try {
            distance = compare.absoluteDistancePercent(firstDataSet,secondDataSet);
        } catch (ComparativeMetricsException e) {
            e.printStackTrace();
        }
        return ((10-distance)>similarity*10 ) ? true : false;
    }

    public Cluster generateNewCluster(SnapshotData newData,double clusterSimilarity){
        List<SnapshotData> listOfData = new ArrayList<>();
        listOfData.add(newData);
        Cluster cluster = new Cluster();
        cluster.setPoints(listOfData);
        cluster.setClusterInterations(1);
        cluster.setSimilarity(clusterSimilarity);
        cluster.setCentroid(newData.getShape());
        cluster.setVolumeCentroid(newData.getVolumeShape());

        return cluster;
    }


    public Integer clusterMinimumRequiredSize(List<Cluster> clusters,int sizeDenominator){
        List<Integer> sizes = new ArrayList<>();
        DescriptiveStatistics statistics = new DescriptiveStatistics();
        for( Cluster cluster : clusters ){
            statistics.addValue(cluster.getPoints().size());
        }
        return (int) (statistics.getMax()/sizeDenominator);
    }

    public void removeClusterNoise(List<Cluster> clusters, int interationsRequiredBeforeDrop,int sizeDenominator){
        //int minimumSize = interationsRequiredBeforeDrop == 0 ? 30 : 3;
        int minimumSize = clusterMinimumRequiredSize(clusters,sizeDenominator);
        //search for noise
        for (Iterator<Cluster> iter = clusters.listIterator(); iter.hasNext(); ) {
            Cluster cluster = iter.next();
            if ( (cluster.getPoints().size() < minimumSize || cluster.getPoints().size()==0) && cluster.getClusterInterations()>interationsRequiredBeforeDrop) {
                iter.remove();
            }
        }
    }
}
