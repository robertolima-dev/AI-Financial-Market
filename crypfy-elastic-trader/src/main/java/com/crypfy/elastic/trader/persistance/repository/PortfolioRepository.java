package com.crypfy.elastic.trader.persistance.repository;

import com.crypfy.elastic.trader.persistance.entity.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PortfolioRepository extends MongoRepository<Portfolio,String> {

    public List<Portfolio> findByMonthAndYearAndPorfolioName(int month, int year, String porfolioName);

}
