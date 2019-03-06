package com.mali.crypfy.clustering.cluster.impl;

import com.mali.crypfy.clustering.cluster.ClusterServices;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.persistence.repository.ClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncClusterUpdate {

    @Autowired
    ClusterServices clusterServices;
    @Autowired
    ClusterRepository clusterRepository;

    @Async
    public void update(Cluster clusterInfo){
        try {
            System.out.println("Updating cluster: "+clusterInfo.getName());
            clusterServices.clusterAndSave(clusterInfo.getShapeSize(),clusterInfo.getnCoins(),clusterInfo.getSimilarity(),clusterInfo.getName(),clusterInfo.getTimeframe(),clusterInfo.getDiscountedCoins(),clusterInfo.getSizeDenominator(),clusterInfo.getStatsSizeFilter());
            clusterServices.setClustersStatistics(clusterRepository.findByName(clusterInfo.getName()));
            clusterServices.removeBadClusters(clusterRepository.findByName(clusterInfo.getName()));
        } catch (CoinException e) {
            e.printStackTrace();
            System.out.println("Não foi possivel atualizar o cluster : "+clusterInfo.getName());
        }

    }

}
