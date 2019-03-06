package com.mali.crypfy.gateway.services.indexmanager;

import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.indexmanager.json.BalancePerDateJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.StatisticsJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.MonthlyPerfomanceJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PlanService {
    public UserPlanJSON add(UserPlanJSON userPlanJSON) throws PlanException;
    public List<UserPlanJSON> getInProgressPlans(String email) throws PlanException;
    public List<UserPlanJSON> list(String email,String status) throws PlanException;
    public List<BalancePerDateJSON> getAllBalanceEvolutionPerPoint(String email) throws PlanException;
    public BigDecimal getTotalProfit(String email) throws PlanException;
    public Long countAll(String email) throws PlanException;
    public StatisticsJSON getPlansStats() throws PlanException;
    public UserPlanJSON changeStatusToInProgress(Integer idplan) throws PlanException;

    public List<MonthlyPerfomanceJSON> getPerfomanceYear(Integer idplan) throws PlanException;
}
