package com.mali.crypfy.clustering.cluster;

import com.mali.crypfy.clustering.api.json.TradeJson;
import com.mali.crypfy.clustering.cluster.exceptions.ComparativeMetricsException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.persistence.entity.Cluster;

import java.util.List;

public interface ClusterServices {

    public void saveImgsFromClusters(int shapeSize) throws CoinException;
    public void clusterAndSave(int shapeArraySize, int maximumAnalizedCoins,double similarity,String name,int timeFrame,int discountedCoins,int sizeDenominator,int statsFilter) throws CoinException;
    public List<TradeJson> findPatterns(String name) throws ComparativeMetricsException;
    public List<String> coinsInCluster(String clusterName);
    public void updateAllClusters();

    //performance
    public void setClustersStatistics(List<Cluster> clusters);
    public void removeBadClusters(List<Cluster> clusters);
}
