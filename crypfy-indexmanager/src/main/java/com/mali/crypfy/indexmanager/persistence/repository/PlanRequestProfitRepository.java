package com.mali.crypfy.indexmanager.persistence.repository;

import com.mali.crypfy.indexmanager.persistence.entity.PlanTakeProfitRequest;
import com.mali.crypfy.indexmanager.persistence.enumeration.PlanRequestProfitStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface PlanRequestProfitRepository extends CrudRepository<PlanTakeProfitRequest,Integer> {
    public List<PlanTakeProfitRequest> findAllByEmailAndIduserPlanAndStatusAndIndexDateBetweenOrderByIndexDateAsc(String email, Integer iduserPlan, PlanRequestProfitStatus status, Date start, Date end);
    public List<PlanTakeProfitRequest> findAllByIduserPlanAndStatusIn(Integer iduserPlan,List<PlanRequestProfitStatus> statuses);
    @EntityGraph(attributePaths = { "userPlan.plan" }, type = EntityGraph.EntityGraphType.LOAD)
    public List<PlanTakeProfitRequest> findAllByEmailOrderByCreatedDesc(String email);
    @EntityGraph(attributePaths = { "userPlan" }, type = EntityGraph.EntityGraphType.LOAD)
    public PlanTakeProfitRequest findByEmailAndIdplanTakeProfitRequest(String email, Integer idplanTakeProfitRequest);
}
