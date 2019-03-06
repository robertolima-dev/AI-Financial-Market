package com.mali.crypfy.clustering.schedulers;

import ch.qos.logback.core.util.FixedDelay;
import com.mali.crypfy.clustering.cluster.ClusterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClusterUpdateScheduler {

    @Autowired
    ClusterServices clusterServices;

    private static final String TIME_ZONE = "America/Sao_Paulo";

    @Scheduled(cron = "0 0 0-23/2 * * *", zone = TIME_ZONE)
    public void upDateClusters() {

        clusterServices.updateAllClusters();

    }

}
