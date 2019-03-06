package com.mali.crawler.api;

import com.mali.crawler.exception.ReadDataCoinException;

import java.util.List;

public interface CoinDataExtractor<T> {
    public List<T> read(String coinName) throws ReadDataCoinException;
}
