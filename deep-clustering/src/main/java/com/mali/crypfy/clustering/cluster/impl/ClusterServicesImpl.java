package com.mali.crypfy.clustering.cluster.impl;

import com.google.common.util.concurrent.Futures;
import com.mali.crypfy.clustering.api.json.TradeJson;
import com.mali.crypfy.clustering.cluster.ClusterServices;
import com.mali.crypfy.clustering.cluster.Clustering;
import com.mali.crypfy.clustering.cluster.ComparativeMetrics;
import com.mali.crypfy.clustering.cluster.exceptions.ComparativeMetricsException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.CoinService;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.persistence.repository.ClusterRepository;
import com.mali.crypfy.clustering.simulations.HistoricalSimulations;
import com.mali.crypfy.clustering.snapshots.SnapshotService;
import com.mali.crypfy.clustering.snapshots.entity.SnapshotData;
import com.mali.crypfy.clustering.utils.DateUtils;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ClusterServicesImpl implements ClusterServices {

    @Autowired
    private SnapshotService snapshotService;
    @Autowired
    private Clustering clustering;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private HistoricalSimulations simulations;
    @Autowired
    private CoinService coinService;
    @Autowired
    private AsyncPatternSearch asyncPatternSearch;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    AsyncClusterUpdate asyncClusterUpdate;

    @Override
    public void saveImgsFromClusters(int shapeSize) {
        int iterator = 0;
        //lets cluster
        for (Cluster cluster : clusterRepository.findByShapeSize(shapeSize)){
            int currentImage = 0;
            for (SnapshotData data : cluster.getPoints()) {
                //string changes
                String[] changes = new String[5];
                for (int iteration = 0; iteration < 5; iteration ++) {
                    double change = data.getChanges()[iteration];
                    int scale = change>=0 ? 4 : 3;
                    BigDecimal bigChange = new BigDecimal(change).setScale(scale,RoundingMode.DOWN);
                    changes[iteration] = bigChange.toString().equals("0.000") ? "0.0000" : bigChange.toString();
                }
                String date = data.getDate().toString().substring(0,10);
                String imgName = "image"+currentImage+"&change1="+changes[0]+"&change2="+changes[1]+"&change3="+changes[2]+"&change4="+changes[3]+"&change5="+changes[4]+"&date="+date;
                snapshotService.generateAndSaveSimplifiedSnapshotImage(data.getImgShape(),imgName, iterator);
                currentImage++;
            }
            iterator++;
        }
    }

    @Override
    public void clusterAndSave(int shapeArraySize, int maximumAnalizedCoins,double similarity, String name,int timeFrame,int discountedCoins,int sizeDenominator,int statsFilter) throws CoinException {
        //delete old clusters
        List<Cluster> oldClusters = clusterRepository.findByName(name);
        if (oldClusters.size()>0) clusterRepository.delete(oldClusters);

        List<SnapshotData> snaps = snapshotService.getClusterAndImgData(shapeArraySize, maximumAnalizedCoins,timeFrame,discountedCoins);
        int interation = 0;
        List<Cluster> clusters = clustering.hierarchicalClustering(snaps,similarity,sizeDenominator);
        //lets cluster
        for (Cluster cluster : clusters){
            cluster.setShapeSize(shapeArraySize);
            cluster.setName(name);
            cluster.setStatsSizeFilter(statsFilter);
            cluster.setTimeframe(timeFrame);
            cluster.setDiscountedCoins(discountedCoins);
            cluster.setnCoins(maximumAnalizedCoins);
            cluster.setSizeDenominator(sizeDenominator);
            clusterRepository.save(cluster);
            interation++;
        }
    }

    public String timeFrameString(int timeFrame){
        if (timeFrame==1) return "PERIOD_5M";
        if (timeFrame==6) return "PERIOD_30M";
        if (timeFrame==12) return "PERIOD_1H";
        if (timeFrame==48) return "PERIOD_4H";
        if (timeFrame==288) return "PERIOD_1D";
        if (timeFrame==2016) return "PERIOD_1W";
        return null;
    }

    @Override
    public List<TradeJson> findPatterns(String name) throws ComparativeMetricsException {
        //vars
        List<TradeJson> tradeJsons = new ArrayList<>();
        List<String> coins = coinsInCluster(name);
        Cluster clusterInfo = clusterRepository.findFirstByName(name);
        List<CompletableFuture<TradeJson>> futures = new ArrayList<>();
        List<Cluster> clusters = clusterRepository.findByName(name);

        for (String coin : coins) {
            futures.add(asyncPatternSearch.check(coin,clusterInfo,name,clusters));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        futures.forEach( future -> future.whenComplete((result, throwable) -> {
            if ( result != null ) tradeJsons.add(result);
        }));

        return tradeJsons;
    }

    public Date correctExpirationDate(int timeFrame, int period) throws CoinException {

        if (timeFrame == 6) return dateUtils.addMinuteFromDate(new Date(),30*period);
        if (timeFrame == 12) return dateUtils.addHourFromDate(new Date(),period);
        if (timeFrame == 48) return dateUtils.addHourFromDate(new Date(),4*period);
        if (timeFrame == 288) return dateUtils.addDayFromDate(new Date(),period);

        throw new CoinException("TimeFrame não encontrado!");
    }

    @Override
    public List<String> coinsInCluster(String clusterName) {
        List<Cluster> clusters = clusterRepository.findByName(clusterName);
        List<String> coins = new ArrayList<>();
        //iterate
        for (Cluster cluster : clusters) {
            List<SnapshotData> dataList = cluster.getPoints();
            for (SnapshotData snapshotData : dataList) if (!coins.contains(snapshotData.getCoinName())) coins.add(snapshotData.getCoinName());
        }
        return coins;
    }

    @Override
    public void updateAllClusters() {
        List<String> clusters = distinctClustersByName();
        for (String clusterName : clusters){
            Cluster clusterInfo = clusterRepository.findByName(clusterName).get(0);
            asyncClusterUpdate.update(clusterInfo);
        }
    }

    public List<String> distinctClustersByName() {
        return mongoTemplate.getCollection("cluster").distinct("name");
    }

    @Override
    public void setClustersStatistics(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            //variables
            double[] changesMeanWinRate = new double[5], changesMeanReturn = new double[5], clusterIndicatedTPs = new double[5], clusterIndicatedSLs = new double[5];
            int pointsSize = cluster.getPoints().size();
            List<SnapshotData> points = cluster.getPoints();
            points.sort(Comparator.comparing(o -> o.getDate()));

            //iteration through points
            for (SnapshotData point : points) {
                for (int iterator = 0; iterator < 5; iterator++) {
                        changesMeanReturn[iterator] = changesMeanReturn[iterator] + point.getChanges()[iterator];
                        changesMeanWinRate[iterator] = point.getChanges()[iterator] > 0 ? changesMeanWinRate[iterator] + 1 : changesMeanWinRate[iterator];
                        clusterIndicatedTPs[iterator] = clusterIndicatedTPs[iterator] + point.getPossibleTPs()[iterator];
                        clusterIndicatedSLs[iterator] = clusterIndicatedSLs[iterator] + point.getPossibleSLs()[iterator];
                }
            }
            //set
            for (int iterator = 0; iterator < 5; iterator++) {
                changesMeanReturn[iterator] = changesMeanReturn[iterator]/pointsSize;
                changesMeanWinRate[iterator] = changesMeanWinRate[iterator]/pointsSize;
                clusterIndicatedTPs[iterator] = clusterIndicatedTPs[iterator]/pointsSize;
                clusterIndicatedSLs[iterator] = clusterIndicatedSLs[iterator]/pointsSize;
            }

            //set the data
            cluster.setChangesMeanReturn(changesMeanReturn);
            cluster.setChangesMeanWinRate(changesMeanWinRate);
            cluster.setClusterIndicatedTPs(clusterIndicatedTPs);
            cluster.setClusterIndicatedSLs(clusterIndicatedSLs);
            //save it
            clusterRepository.save(cluster);
        }
    }

    @Override
    public void removeBadClusters(List<Cluster> clusters) {

        for (Cluster cluster : clusters){
            boolean isBad = true;
            for (Double change : cluster.getChangesMeanReturn()){
                if (change>0.02) isBad = false;
            }
            //is bad?
            if (isBad){
                clusterRepository.delete(cluster);
            }else if (cluster.getPoints().size()<10) clusterRepository.delete(cluster);
        }
    }



}
