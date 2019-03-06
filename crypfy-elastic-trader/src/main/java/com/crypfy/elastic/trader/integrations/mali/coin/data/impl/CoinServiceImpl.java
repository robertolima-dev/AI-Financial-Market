package com.crypfy.elastic.trader.integrations.mali.coin.data.impl;

import com.crypfy.elastic.trader.integrations.mali.coin.data.CoinService;
import com.crypfy.elastic.trader.integrations.mali.coin.data.exception.CoinException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinJson;
import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.rest.json.RestResponseListJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Value("${spring.services.mali-coin-data.coins}")
    private String getCoinsListService;
    @Autowired
    MessageSender msgSender;

    @Override
    public List<CoinJson> findByMarketCapDesc(Integer requiredCoins) throws CoinException {

        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<RestResponseListJSON<CoinJson>> response = restTemplate.exchange(getCoinsListService+requiredCoins, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<CoinJson>>() {});

            //if execution not 200/201/20
            if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
                throw new CoinException("Ocorreu um erro no servidor");

            RestResponseListJSON<CoinJson> restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return (List<CoinJson>) restResponseJSON.getResponse();
            else
                throw new CoinException(restResponseJSON.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            msgSender.sendMsg("Problemas de conexão com o projeto coin data!");
            throw new CoinException("Problemas de conexão com o projeto coin data!");
        }
    }
}
