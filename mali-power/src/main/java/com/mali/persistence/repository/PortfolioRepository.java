package com.mali.persistence.repository;

import com.mali.persistence.entity.Portfolio;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PortfolioRepository extends CrudRepository<Portfolio,Integer>{
    public List<Portfolio> findAll();
    public Portfolio findTopByOrderByCreateByDesc();
}
