package com.crypfy.elastic.trader.integrations.deep.clustering.impl;

import com.crypfy.elastic.trader.integrations.ServiceItemError;
import com.crypfy.elastic.trader.integrations.deep.clustering.ClusterServices;
import com.crypfy.elastic.trader.integrations.deep.clustering.exception.ClusterException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinHistoryJson;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.rest.json.RestResponseJSON;
import com.crypfy.elastic.trader.rest.json.RestResponseListJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusterServicesImpl implements ClusterServices {

    @Value("${spring.services.cluster.check-and-save}")
    private String checkAndSaveService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Order> checkForPatterns(String clusterName) throws ClusterException {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseListJSON<Order>> response =  restTemplate.exchange(checkAndSaveService+"?name="+clusterName, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<Order>>() {});

        //if execution not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new ClusterException("Ocorreu um erro no servidor");

        RestResponseListJSON<Order> restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<Order>) restResponseJSON.getResponse();
        else {
            List<ServiceItemError> errors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new ClusterException(restResponseJSON.getMessage(),errors,restResponseJSON.getStatus());
        }
    }
}
