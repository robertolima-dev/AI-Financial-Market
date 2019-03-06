package com.mali.crypfy.clustering.api;

import com.mali.crypfy.clustering.api.json.TradeJson;
import com.mali.crypfy.clustering.cluster.ClusterServices;
import com.mali.crypfy.clustering.cluster.Clustering;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.RestResponseJSON;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.persistence.repository.ClusterRepository;
import com.mali.crypfy.clustering.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
public class ClusterRest {

    @Autowired
    ClusterServices clusterServices;
    @Autowired
    ClusterRepository clusterRepository;
    @Autowired
    DateUtils dateUtils;

    @RequestMapping(value = "clusters/generate",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON>  generateClusters(@RequestParam int nCoins, @RequestParam int arraySize, @RequestParam double similarity, @RequestParam int timeFrame, @RequestParam int discountedCoins, @RequestParam int sizeDenominator, @RequestParam String name,@RequestParam int statsFilter){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            clusterServices.clusterAndSave(arraySize,nCoins,similarity,name,timeFrame,discountedCoins,sizeDenominator,statsFilter);
            clusterServices.setClustersStatistics(clusterRepository.findByShapeSizeAndName(arraySize,name));
            clusterServices.removeBadClusters(clusterRepository.findByShapeSizeAndName(arraySize,name));
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Clusters gerados com sucesso!");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "clusters/generate-images",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON>  generateImages(@RequestParam int arraySize){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            clusterServices.saveImgsFromClusters(arraySize);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "clusters",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON>  getClusters(@RequestParam int arraySize){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<Cluster> clusters = clusterRepository.findByShapeSize(arraySize);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(clusters);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "clusters/patterns",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON>  checkPatterns(@RequestParam String name){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<TradeJson> trade = clusterServices.findPatterns(name);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Operação realizada com sucesso");
            restResponseJSON.setResponse(trade);
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
