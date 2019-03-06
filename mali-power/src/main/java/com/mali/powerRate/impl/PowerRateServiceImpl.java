package com.mali.powerRate.impl;

import com.mali.persistence.entity.PowerRate;
import com.mali.persistence.repository.PowerRateRepository;
import com.mali.powerRate.api.PowerRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerRateServiceImpl implements PowerRateService {

    @Autowired
    PowerRateRepository powerRateRepository;

    @Override
    public List<PowerRate> getAll() {
        return powerRateRepository.findAll();
    }

    @Override
    public List<PowerRate> listByCategory(String category) {
        return powerRateRepository.findByCategory(category);
    }
}
