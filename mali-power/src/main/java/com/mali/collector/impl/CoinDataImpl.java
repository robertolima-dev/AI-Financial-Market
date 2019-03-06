package com.mali.collector.impl;

import com.mali.collector.api.CoinData;
import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.collector.json.CoinHistoryJson;
import com.mali.collector.json.CoinJson;
import com.mali.config.maliCoin.ConfigMaliCoinServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoinDataImpl implements CoinData {

    @Autowired
    private ConfigMaliCoinServices configMaliCoinServices;

    @Override
    public List<CoinHistoryJson> coinHistoryByIdcoin(String idcoin) throws CoinDataAPIException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CoinHistoryJson[]> coins = restTemplate.getForEntity(configMaliCoinServices.getListCoinHistoryByIdcoinService() + idcoin, CoinHistoryJson[].class);
            return new ArrayList<CoinHistoryJson>(Arrays.asList(coins.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoinDataAPIException("error on consumes coin data api");
        }
    }

    @Override
    public List<CoinJson> all() throws CoinDataAPIException {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CoinJson[]> coins = restTemplate.getForEntity(configMaliCoinServices.getListCoinsService(), CoinJson[].class);
            return new ArrayList<CoinJson>(Arrays.asList(coins.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoinDataAPIException("error on consumes coin data api");
        }
    }

    @Override
    public List<CoinJson> coinByMarketCap(BigDecimal marketCap) throws CoinDataAPIException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CoinJson[]> coins = restTemplate.getForEntity(configMaliCoinServices.getListCoinsByMarketCapService()+marketCap, CoinJson[].class);
            return new ArrayList<CoinJson>(Arrays.asList(coins.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoinDataAPIException("error on consumes coin data api");
        }
    }
}
