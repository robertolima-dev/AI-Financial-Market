package com.mali.crypfy.indexmanager.core;

import com.mali.crypfy.indexmanager.core.exception.PlanException;
import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PlanService {
    public List<UserPlan> list(String email, UserPlanStatus status) throws PlanException;
    public List<UserPlan> getInProgressPlans(String email) throws PlanException;
    public List<BalancePerDate> getAllBalanceEvolutionPerPoint(String email) throws PlanException;
    public BigDecimal getTotalProfit(String email) throws PlanException;
    public Long countAllByEmail(String email) throws PlanException;
    public UserPlan add(UserPlan userPlan) throws PlanException;
    public UserPlan changeStatusToInProgress(Integer iduserPlan) throws PlanException;
    public UserPlan calcDetailedBalancePlan(UserPlan plan);
    public UserPlan calcCurrentBalancePlan(UserPlan plan);
    public boolean checkProfit(Integer id,BigDecimal targetProfit) throws PlanException;

    //for admin use
    public Statistics getPlansStats() throws PlanException;
    public List<MonthlyPerfomance> getPerfomancePlanMonthly(int idplan, Date start, Date end) throws PlanException;

    public UserPlan find(Integer id);

    public List<TickerInfo> buidTickerInfo(String email) throws PlanException;
}
