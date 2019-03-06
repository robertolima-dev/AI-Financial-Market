package com.mali.powerRate.api;

import com.mali.persistence.entity.PowerRate;
import java.util.List;

public interface PowerRateService {
    public List<PowerRate> getAll();
    public List<PowerRate> listByCategory(String category);
}
