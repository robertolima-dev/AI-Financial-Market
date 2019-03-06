package com.crypfy.persistence.repository;

import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoricalCoinSnapshotRepository extends CrudRepository<HistoricalCoinSnapshot,String>{

    public List<HistoricalCoinSnapshot> findByWeakId(int weakId);

}
