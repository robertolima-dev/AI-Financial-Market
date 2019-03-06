package com.mali.persistence.repository;

import com.mali.persistence.entity.Coin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CoinRepository extends CrudRepository<Coin,String> {

    @EntityGraph(value = "Coin.detail", type = EntityGraph.EntityGraphType.FETCH)
    public List<Coin> findAllByOrderByMarketCapUsdDesc();
    @EntityGraph(value = "Coin.detail", type = EntityGraph.EntityGraphType.FETCH)
    public List<Coin> findByMarketCapUsdGreaterThanOrderByMarketCapUsdDesc(BigDecimal marketCap);
}
