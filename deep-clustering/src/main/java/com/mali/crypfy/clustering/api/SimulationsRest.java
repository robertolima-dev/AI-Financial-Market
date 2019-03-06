package com.mali.crypfy.clustering.api;

import com.mali.crypfy.clustering.integrations.mali.coin.data.json.RestResponseJSON;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.persistence.repository.ClusterRepository;
import com.mali.crypfy.clustering.simulations.HistoricalSimulations;
import com.mali.crypfy.clustering.simulations.SimpleStats;
import com.mali.crypfy.clustering.simulations.SimulationStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SimulationsRest {

    @Autowired
    HistoricalSimulations simulations;
    @Autowired
    ClusterRepository clusterRepository;

    @RequestMapping(value = "clusters/simulation",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> generateSimulation(@RequestParam int transactions,@RequestParam int start, @RequestParam double balance, @RequestParam int end,@RequestParam int minCalls,@RequestParam int timeFrame,@RequestParam String name){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<Cluster> clusters  = clusterRepository.findByName(name);
            SimulationStatistics statistics = simulations.doHistoricalSimulation(clusters,balance,transactions,start,end,minCalls,timeFrame);

            /*
            List<Cluster> clusters = clusterRepository.findByShapeSizeAndName(arraySize,name);
            List<SimpleStats> stats = new ArrayList<>();
            for (Cluster cluster : clusters){
                List<Cluster> correctClusters = new ArrayList<>();
                correctClusters.add(cluster);
                SimulationStatistics statistics = simulations.doHistoricalSimulation(correctClusters,balance,transactions,start,end,minCalls,timeFrame);
                SimpleStats simpleStats = new SimpleStats();
                simpleStats.setFinalBalance(statistics.getFinalBalance());
                simpleStats.setMaxDD(statistics.getMaxDD());
                simpleStats.setnTransactions(statistics.getnTransactions());
                stats.add(simpleStats);
            }*/

            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(statistics);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

}
