package com.mali.exchanger.communication.impl.cryptocompare.timeFrame.impl;

import com.mali.entity.CandleHistory;
import com.mali.entity.Market;
import com.mali.exchanger.communication.impl.cryptocompare.CryptoCompareApis;
import com.mali.exchanger.communication.impl.cryptocompare.MarketHistoryByTimeFrame;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareApiException;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareMarketHistoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyHistoryImpl implements MarketHistoryByTimeFrame {

    @Autowired
    CryptoCompareApis cryptoCompareApis;

    @Override
    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, String exchangeName) throws CryptocompareMarketHistoryException {
        CandleHistory candleHistory = new CandleHistory();
        candleHistory.setMarket(market);
        try {
            candleHistory.setHistory(cryptoCompareApis.getDailyHistory(exchangeName,market,nBars,timeframeMultiplier).getHistory());
        } catch (CryptocompareApiException e) {
            e.printStackTrace();
            throw new CryptocompareMarketHistoryException("Erro na implementaçao markets do cryptocompare",e.getErrors());
        }
        return candleHistory;
    }

}
