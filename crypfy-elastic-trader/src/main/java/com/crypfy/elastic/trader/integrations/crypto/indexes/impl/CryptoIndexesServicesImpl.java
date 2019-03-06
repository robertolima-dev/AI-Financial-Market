package com.crypfy.elastic.trader.integrations.crypto.indexes.impl;

import com.crypfy.elastic.trader.integrations.ServiceItemError;
import com.crypfy.elastic.trader.integrations.crypto.indexes.IndexesServices;
import com.crypfy.elastic.trader.integrations.crypto.indexes.exceptions.CryptoIndexesException;
import com.crypfy.elastic.trader.integrations.crypto.indexes.json.Index;
import com.crypfy.elastic.trader.rest.json.RestResponseJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CryptoIndexesServicesImpl implements IndexesServices {

    @Value("${spring.services.crypto-indexes.last-update}")
    private String lastUpdateService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Index getLastUpdate(String source,String asset) throws CryptoIndexesException {

        String url = lastUpdateService.replace("{source}",source.toUpperCase());
        url = url.replace("asset",asset);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(url, HttpMethod.GET,null,RestResponseJSON.class);

        //if execution not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new CryptoIndexesException("Ocorreu um erro no servidor");

        RestResponseJSON restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess())
            return mapper.convertValue(restResponseJSON.getResponse(),Index.class);
        else {
            List<ServiceItemError> errors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new CryptoIndexesException(restResponseJSON.getMessage(),errors,restResponseJSON.getStatus());
        }

    }
}
