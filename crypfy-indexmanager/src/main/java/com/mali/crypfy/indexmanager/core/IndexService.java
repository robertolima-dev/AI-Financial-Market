package com.mali.crypfy.indexmanager.core;

import com.mali.crypfy.indexmanager.persistence.entity.IndexPlan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IndexService {

    public List<IndexPlan> getIndexesByIdPlan(Integer idPlan);
    public BigDecimal getPerformanceBetweenDates(Integer idplan, Date start, Date end);
    public IndexPlan getFirstIndexByDate(Integer idplan,Date date);
    public IndexPlan findIndexByDate(Integer idplan, Date date);

}
