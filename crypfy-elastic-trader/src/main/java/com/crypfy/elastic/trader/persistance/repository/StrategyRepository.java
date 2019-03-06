package com.crypfy.elastic.trader.persistance.repository;

import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StrategyRepository extends MongoRepository<Strategy,String> {

    public Strategy findByName(String name);
    public List<Strategy> findByStrategyStatusNot(StrategyStatus strategyStatus);

}
