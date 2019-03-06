package com.mali.coin.api;

import com.mali.coin.exceptions.CoinException;
import com.mali.persistence.entity.CoinHistory;

import java.util.List;

public interface CoinHistoryService {
    public List<CoinHistory> listByIdicon(String idIcon, int requiredElements) throws CoinException;
}
