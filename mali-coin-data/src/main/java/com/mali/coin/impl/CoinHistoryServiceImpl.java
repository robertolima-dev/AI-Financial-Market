package com.mali.coin.impl;

import com.mali.coin.api.CoinHistoryService;
import com.mali.coin.exceptions.CoinException;
import com.mali.persistence.entity.CoinHistory;
import com.mali.persistence.repository.CoinHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinHistoryServiceImpl implements CoinHistoryService {

    @Autowired
    private CoinHistoryRepository coinHistoryRepository;

    @Override
    public List<CoinHistory> listByIdicon(String idIcon, int requiredElements) throws CoinException {
        //variables
        List<CoinHistory> fullList = coinHistoryRepository.findByIdcoin(idIcon);
        List<CoinHistory> coinHistories = new ArrayList<>();

        if (requiredElements!=0) {
            if (fullList.size() < requiredElements)
                throw new CoinException("Não existe histórico suficiente dessa moeda");
            for (int iterator = 0; iterator < requiredElements; iterator++) coinHistories.add(fullList.get(iterator));
        }else return fullList;

        return coinHistories;
    }
}
