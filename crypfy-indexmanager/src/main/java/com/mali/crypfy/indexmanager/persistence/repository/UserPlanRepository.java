package com.mali.crypfy.indexmanager.persistence.repository;

import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserPlanRepository extends CrudRepository<UserPlan,Integer>,UserPlanCustomRepository {
    @EntityGraph(attributePaths = { "plan" }, type = EntityGraph.EntityGraphType.LOAD)
    public List<UserPlan> findByEmailAndStatusOrderByStartDateDesc(String email, UserPlanStatus status);
    @EntityGraph(attributePaths = { "plan" }, type = EntityGraph.EntityGraphType.LOAD)
    public List<UserPlan> findAllByEmailOrderByCreatedDesc(String email);
    public List<UserPlan> findAllByEmailAndStatusInOrderByCreatedDesc(String email,List<UserPlanStatus> status);
    public Long countAllByEmail(String email);
    public UserPlan findByiduserPlan(int iduserPlan);
    @EntityGraph(attributePaths = { "plan" }, type = EntityGraph.EntityGraphType.LOAD)
    public UserPlan findByIduserPlanAndEmail(Integer iduserPlan, String email);

    //admin
    @EntityGraph(attributePaths = { "plan" }, type = EntityGraph.EntityGraphType.LOAD)
    public List<UserPlan> findByStatusOrderByStartDateDesc(UserPlanStatus status);
    public Long countAllByStatus(UserPlanStatus status);
}
