package com.crypfy.elastic.trader.integrations.mali.coin.data.impl;

import com.crypfy.elastic.trader.integrations.mali.coin.data.CoinHistoryService;
import com.crypfy.elastic.trader.integrations.mali.coin.data.exception.CoinException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinHistoryJson;
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
public class CoinHistoryServiceImpl implements CoinHistoryService {

    @Value("${spring.services.mali-coin-data.coin-history}")
    private String getCoinHistoryService;
    @Autowired
    MessageSender msgSender;

    @Override
    public List<CoinHistoryJson> historyByCoin(String idIcon,int timeFrame,int limit) throws CoinException {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = getCoinHistoryService.replace("{coinName}",idIcon);
            url = url.replace("{timeFrame}",String.valueOf(timeFrame));
            ResponseEntity<RestResponseListJSON<CoinHistoryJson>> response = restTemplate.exchange(url+limit, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<CoinHistoryJson>>() {});

            //if execution not 200/201/20
            if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
                throw new CoinException("Ocorreu um erro no servidor");

            RestResponseListJSON<CoinHistoryJson> restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return (List<CoinHistoryJson>) restResponseJSON.getResponse();
            else {
                System.out.println(url);
                throw new CoinException(restResponseJSON.getMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
            msgSender.sendMsg("Problemas de conexão com o projeto coin data!");
            throw new CoinException("Problemas de conexão com o projeto coin data!");
        }


    }
}
