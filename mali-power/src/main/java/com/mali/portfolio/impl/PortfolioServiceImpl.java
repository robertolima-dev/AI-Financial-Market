package com.mali.portfolio.impl;

import com.mali.persistence.entity.Portfolio;
import com.mali.persistence.repository.PortfolioRepository;
import com.mali.portfolio.api.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService{

    @Autowired
    PortfolioRepository portfolioRepository;

    @Override
    public List<Portfolio> getAll() {
        return portfolioRepository.findAll();
    }

    @Override
    public Portfolio currentPortfolio() {
        return portfolioRepository.findTopByOrderByCreateByDesc();
    }
}
