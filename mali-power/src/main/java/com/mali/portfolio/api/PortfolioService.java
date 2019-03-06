package com.mali.portfolio.api;

import com.mali.persistence.entity.Portfolio;

import java.util.List;

public interface PortfolioService {
    public List<Portfolio> getAll();
    public Portfolio currentPortfolio();
}
