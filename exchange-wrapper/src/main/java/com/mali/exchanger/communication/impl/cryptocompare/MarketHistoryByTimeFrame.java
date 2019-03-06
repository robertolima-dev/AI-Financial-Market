package com.mali.exchanger.communication.impl.cryptocompare;

import com.mali.entity.CandleHistory;
import com.mali.entity.Market;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareMarketHistoryException;

public interface MarketHistoryByTimeFrame {

    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, String exchangeName) throws CryptocompareMarketHistoryException;

}
