package com.mali.crypfy.indexmanager.persistence.repository;

import com.mali.crypfy.indexmanager.persistence.entity.IndexPlan;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IndexPlanRepository extends CrudRepository<IndexPlan,Integer> {
    public IndexPlan findFirst1ByIdplanOrderByUpdatedDesc(Integer idplan);
    public IndexPlan findByIdplanAndUpdated(Integer idplan,Date date);
    @CacheEvict(value = "cacheName", allEntries = true)
    public List<IndexPlan> findAllByIdplanAndUpdatedBetweenOrderByUpdatedAsc(Integer idplan,Date startDate, Date endDate);
    public IndexPlan findFirst1ByIdplanAndUpdatedGreaterThanEqual(Integer idplan,Date date);
    public IndexPlan findFirst1ByIdplanAndUpdatedLessThanEqualOrderByUpdatedDesc(Integer idplan,Date date);
    @Query("SELECT i FROM IndexPlan i where DATE(i.updated) = :date and i.idplan = :idplan ORDER BY i.updated ASC")
    public List<IndexPlan> getIndexByDate(@Param("idplan") Integer idplan,@Param("date") Date date);
    @Query("Select i from IndexPlan i where (i.updated >= :dateStart and i.updated <= :dateEnd and idplan = :idplan) or (i.updated = :dateStart and idplan = :idplan) order by i.updated ASC")
    public List<IndexPlan> getMonthlyIndexBreakPoints(@Param("idplan") Integer idplan,@Param("dateStart") Date dateStart, @Param("dateEnd")Date dateEnd);
    public List<IndexPlan> findAllByIdplanAndUpdatedIn(Integer idplan, List<Date> dates);

    public List<IndexPlan> findByIdplanOrderByUpdatedDesc(Integer idplan);
}
