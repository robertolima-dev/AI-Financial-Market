package com.mali.persistence.repository;

import com.mali.persistence.entity.Coin;
import com.mali.persistence.entity.CoinHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoinHistoryRepository extends CrudRepository<CoinHistory,Long> {
    public List<CoinHistory> findByIdcoin(String idcoin);
    public List<CoinHistory> findAll();
}
