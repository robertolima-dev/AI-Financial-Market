package com.mali.persistence.repository;

import com.mali.persistence.entity.PowerRate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PowerRateRepository extends CrudRepository<PowerRate,String>{
    public List<PowerRate> findByCategory(String category);
    public List<PowerRate> findAll();
}
