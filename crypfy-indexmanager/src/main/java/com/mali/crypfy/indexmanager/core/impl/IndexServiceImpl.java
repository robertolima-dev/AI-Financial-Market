package com.mali.crypfy.indexmanager.core.impl;

import com.mali.crypfy.indexmanager.core.IndexService;
import com.mali.crypfy.indexmanager.persistence.entity.IndexPlan;
import com.mali.crypfy.indexmanager.persistence.repository.IndexPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    IndexPlanRepository indexPlanRepository;

    @Override
    public List<IndexPlan> getIndexesByIdPlan(Integer idPlan) {
        return indexPlanRepository.findByIdplanOrderByUpdatedDesc(idPlan);
    }

    @Override
    public BigDecimal getPerformanceBetweenDates(Integer idplan, Date start, Date end) {
        IndexPlan startIndex = indexPlanRepository.findFirst1ByIdplanAndUpdatedGreaterThanEqual(idplan, start);
        IndexPlan endIndex = indexPlanRepository.findFirst1ByIdplanAndUpdatedGreaterThanEqual(idplan, end);
        if (endIndex == null)
            endIndex = indexPlanRepository.findFirst1ByIdplanAndUpdatedLessThanEqualOrderByUpdatedDesc(idplan, end);

        if (startIndex == null || endIndex == null) return BigDecimal.ZERO;

        BigDecimal last = startIndex.getIndex(), first = endIndex.getIndex();
        return ((first.subtract(last)).divide(last, 4, RoundingMode.HALF_DOWN)).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_DOWN);
    }

    @Override
    public IndexPlan getFirstIndexByDate(Integer idplan, Date date) {
        return indexPlanRepository.findFirst1ByIdplanAndUpdatedGreaterThanEqual(idplan, date);
    }

    @Override
    public IndexPlan findIndexByDate(Integer idplan, Date date) {
        return this.indexPlanRepository.findByIdplanAndUpdated(idplan,date);
    }
}
