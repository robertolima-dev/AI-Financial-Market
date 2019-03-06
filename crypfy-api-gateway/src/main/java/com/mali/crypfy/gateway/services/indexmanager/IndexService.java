package com.mali.crypfy.gateway.services.indexmanager;

import com.mali.crypfy.gateway.services.indexmanager.exceptions.IndexException;
import com.mali.crypfy.gateway.services.indexmanager.json.IndexPlanJSON;

import java.math.BigDecimal;
import java.util.Date;

public interface IndexService {
    public BigDecimal getPerfomanceByDate(Integer idplan, Date start, Date end) throws IndexException;
    public IndexPlanJSON getFirstByDate(Integer idplan, Date date) throws IndexException;
}
