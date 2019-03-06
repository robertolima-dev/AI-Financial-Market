package com.mali.coin.impl;

import com.mali.coin.api.CoinService;
import com.mali.coin.coinmarketcap.CoinMarketCapAPI;
import com.mali.coin.coinmarketcap.CoinMarketCapTicker;
import com.mali.coin.coinmarketcap.exceptions.CoinMarketCapAPIException;
import com.mali.coin.exceptions.CoinException;
import com.mali.persistence.entity.Coin;
import com.mali.persistence.entity.CoinHistory;
import com.mali.persistence.enumeration.CoinStatus;
import com.mali.persistence.repository.CoinHistoryRepository;
import com.mali.persistence.repository.CoinRepository;
import com.mali.validators.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinMarketCapAPI coinMarketCapAPI;
    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    CoinHistoryRepository coinHistoryRepository;

    @Override
    public void addOrUpdateCoins() throws CoinMarketCapAPIException {

        try {
            //listing all coins(market cap coin)
            List<CoinMarketCapTicker> coinMarketCapTickers = coinMarketCapAPI.readCoins();

            for(CoinMarketCapTicker ticker : coinMarketCapTickers) {
                Coin foundCoin = coinRepository.findOne(ticker.getId());

                //if coin already exist, update
                if(foundCoin != null) {

                    if(ticker.getName() != null)
                        foundCoin.setName(ticker.getName());
                    if(ticker.getSymbol() != null)
                        foundCoin.setSymbol(ticker.getSymbol());
                    if(ticker.getPriceUsd() != null)
                        foundCoin.setPriceUsd(new BigDecimal(ticker.getPriceUsd()));
                    if(ticker.getPriceBtc() != null)
                        foundCoin.setPriceBtc(new BigDecimal(ticker.getPriceBtc()));
                    if(ticker.getVolumeUsd24h() != null)
                        foundCoin.setVolume24hUsd(new BigDecimal(ticker.getVolumeUsd24h()));
                    if(ticker.getMarketCapUsd() != null)
                        foundCoin.setMarketCapUsd(new BigDecimal(ticker.getMarketCapUsd()));
                    if(ticker.getAvailableSupply() != null)
                        foundCoin.setSupplyAvailable(new BigDecimal(ticker.getAvailableSupply()));
                    if(ticker.getTotalSupply() != null)
                        foundCoin.setSupplyTotal(new BigDecimal(ticker.getTotalSupply()));
                    if(ticker.getPercentChange1h() != null)
                        foundCoin.setPercentChange1h(new BigDecimal(ticker.getPercentChange1h()));
                    if(ticker.getPercentChange24h() != null)
                        foundCoin.setPercentChange24h(new BigDecimal(ticker.getPercentChange24h()));
                    if(ticker.getPercentChange7d() != null)
                        foundCoin.setPercentChange7d(new BigDecimal(ticker.getPercentChange7d()));
                    if(ticker.getLastUpated() != null)
                        foundCoin.setLastUpdated(ticker.getLastUpated());

                    //update
                    Coin updatedCoin = coinRepository.save(foundCoin);
                } else {
                    Coin newCoin = new Coin();

                    if(ticker.getId() != null)
                        newCoin.setIdcoin(ticker.getId());
                    if(ticker.getName() != null)
                        newCoin.setName(ticker.getName());
                    if(ticker.getSymbol() != null)
                        newCoin.setSymbol(ticker.getSymbol());
                    if(ticker.getPriceUsd() != null)
                        newCoin.setPriceUsd(new BigDecimal(ticker.getPriceUsd()));
                    if(ticker.getPriceBtc() != null)
                        newCoin.setPriceBtc(new BigDecimal(ticker.getPriceBtc()));
                    if(ticker.getVolumeUsd24h() != null)
                        newCoin.setVolume24hUsd(new BigDecimal(ticker.getVolumeUsd24h()));
                    if(ticker.getMarketCapUsd() != null)
                        newCoin.setMarketCapUsd(new BigDecimal(ticker.getMarketCapUsd()));
                    if(ticker.getAvailableSupply() != null)
                        newCoin.setSupplyAvailable(new BigDecimal(ticker.getAvailableSupply()));
                    if(ticker.getTotalSupply() != null)
                        newCoin.setSupplyTotal(new BigDecimal(ticker.getTotalSupply()));
                    if(ticker.getPercentChange1h() != null)
                        newCoin.setPercentChange1h(new BigDecimal(ticker.getPercentChange1h()));
                    if(ticker.getPercentChange24h() != null)
                        newCoin.setPercentChange24h(new BigDecimal(ticker.getPercentChange24h()));
                    if(ticker.getPercentChange7d() != null)
                        newCoin.setPercentChange7d(new BigDecimal(ticker.getPercentChange7d()));
                    if(ticker.getLastUpated() != null)
                        newCoin.setLastUpdated(ticker.getLastUpated());

                    //set status not verified
                    newCoin.setStatus(CoinStatus.NOT_VERIFIED);

                    //save new coin
                    coinRepository.save(newCoin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Coin add(Coin coin) throws CoinException {

        List<FieldError> errors = new ArrayList<FieldError>();
        //errors.add(new FieldError("campo 1","campo 1 não pode ser vazio"));
        //errors.add(new FieldError("campo 2","campo 2 não pode ser vazio"));

        if(!errors.isEmpty())
            throw new CoinException("ocorre um erro ao adicionar a coin",errors);

        return coinRepository.save(coin);
    }

    @Override
    public List<Coin> findAllByOrderByMarketCapUsdDesc(int requiredElements) {
        //vars
        List<Coin> fullList = coinRepository.findAllByOrderByMarketCapUsdDesc();
        List<Coin> coins = new ArrayList<>();

        //iterate
        for (int iterator = 0; iterator < requiredElements; iterator ++) coins.add(fullList.get(iterator));

        return coins;
    }

    @Override
    public List<Coin> findByMarketCapUsdGreaterThan(BigDecimal marketCap) {
        return coinRepository.findByMarketCapUsdGreaterThanOrderByMarketCapUsdDesc(marketCap);
    }

    @Override
    public List<String> findByHistory(int requiredElements) {

        List<CoinHistory> histories = coinHistoryRepository.findAll();
        List<String> coins = new ArrayList<>();
        int nCoins = 0;

        for (CoinHistory history : histories) {
            if (nCoins==requiredElements) break;
            if (!coins.contains(history.getIdcoin())) {
                coins.add(history.getIdcoin());
                nCoins++;
            }
        }

        return nCoins==requiredElements ? coins : null;
    }
}
