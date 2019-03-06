package com.mali.crypfy.indexmanager.core;

import com.mali.crypfy.indexmanager.core.exception.AccountNotVerifiedException;
import com.mali.crypfy.indexmanager.core.exception.PlanRequestProfitAddAllException;
import com.mali.crypfy.indexmanager.core.exception.PlanRequestProfitException;
import com.mali.crypfy.indexmanager.persistence.entity.PlanTakeProfitRequest;

import java.util.Date;
import java.util.List;

public interface PlanRequestProfitService {
    public List<PlanTakeProfitRequest> getIndexBreakPoints(String email, Integer iduserPlan, Date start, Date end);
    public PlanTakeProfitRequest add(PlanTakeProfitRequest planTakeProfitRequest) throws PlanRequestProfitException,AccountNotVerifiedException;
    public void addAll(List<PlanTakeProfitRequest> planTakeProfitRequests) throws PlanRequestProfitAddAllException,AccountNotVerifiedException;
    public List<PlanTakeProfitRequest> list(String email) throws PlanRequestProfitException;
    public PlanTakeProfitRequest changeStatusToCancelled(String email, Integer idplanRequestProfit) throws PlanRequestProfitException;
    public PlanTakeProfitRequest changeStatusToProcessing(String email, Integer idplanRequestProfit) throws PlanRequestProfitException;
    public PlanTakeProfitRequest changeStatusToFailed(String email, Integer idplanRequestProfit, String failedReason) throws PlanRequestProfitException;
    public PlanTakeProfitRequest changeStatusToConfirmed(String email, Integer idplanRequestProfit, Date indexDate) throws PlanRequestProfitException;
    public void delete(String email, Integer idplanRequestProfit) throws PlanRequestProfitException;
}
