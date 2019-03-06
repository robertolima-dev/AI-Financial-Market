package com.crypfy.crawler.api;

import com.crypfy.crawler.exception.ReadDataCoinException;
import com.crypfy.persistence.entity.HistoricalCoinSnapshot;

import java.util.List;

public interface CoinDataExtractor {
    public List<HistoricalCoinSnapshot> read(String dateString,int weak) throws ReadDataCoinException;
}
