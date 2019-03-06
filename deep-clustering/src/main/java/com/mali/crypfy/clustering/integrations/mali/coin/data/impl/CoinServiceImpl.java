package com.mali.crypfy.clustering.integrations.mali.coin.data.impl;

import com.mali.crypfy.clustering.integrations.mali.coin.data.CoinService;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinJson;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.RestResponseJSON;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.RestResponseListJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CoinServiceImpl implements CoinService {

    @Value("${spring.services.coin-data.coins}")
    private String getCoinsListService;
    @Value("${spring.services.coin-data.coin-history}")
    private String getCoinHistoryService;

    @Override
    public List<CoinJson> coinsByMarketCapDesc(Integer requiredCoins) throws CoinException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RestResponseListJSON<CoinJson>> response = restTemplate.exchange(getCoinsListService+requiredCoins, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<CoinJson>>() {});

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new CoinException("Ocorreu um erro no servidor");

        RestResponseListJSON<CoinJson> restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<CoinJson>) restResponseJSON.getResponse();
        else
            throw new CoinException(restResponseJSON.getMessage());
    }


    @Override
    public List<CoinHistoryJson> historyByCoin(String idIcon,int timeFrame,int limit,String order) throws CoinException {
        RestTemplate restTemplate = new RestTemplate();

        String url = getCoinHistoryService.replace("{coinName}",idIcon);
        url = url.replace("{timeFrame}",String.valueOf(timeFrame));
        url = url.replace("{order}",order.toLowerCase());

        ResponseEntity<RestResponseListJSON<CoinHistoryJson>> response = restTemplate.exchange(url+limit, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<CoinHistoryJson>>() {});

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new CoinException("Ocorreu um erro no servidor");

        RestResponseListJSON<CoinHistoryJson> restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<CoinHistoryJson>) restResponseJSON.getResponse();
        else
            throw new CoinException(restResponseJSON.getMessage());

    }
}
