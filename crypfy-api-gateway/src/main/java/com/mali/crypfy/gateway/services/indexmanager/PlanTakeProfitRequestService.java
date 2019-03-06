package com.mali.crypfy.gateway.services.indexmanager;

import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanTakeProfitRequestException;
import com.mali.crypfy.gateway.services.indexmanager.json.PlanTakeProfitRequestJSON;

import java.util.Date;
import java.util.List;

public interface PlanTakeProfitRequestService {
    public void addAll(List<PlanTakeProfitRequestJSON> planTakeProfitRequestsJSON) throws PlanTakeProfitRequestException;
    public List<PlanTakeProfitRequestJSON> list(String email) throws PlanTakeProfitRequestException;
    public PlanTakeProfitRequestJSON changeStatusToCancelled(String email, Integer id) throws PlanTakeProfitRequestException;
    public PlanTakeProfitRequestJSON changeStatusToFailed(Integer id, String failedReason) throws PlanTakeProfitRequestException;
    public PlanTakeProfitRequestJSON changeStatusToProcessing(Integer id) throws PlanTakeProfitRequestException;
    public PlanTakeProfitRequestJSON changeStatusToConfirmed(Integer id, Date indexDate) throws PlanTakeProfitRequestException;
    public void delete(String email, Integer id) throws PlanTakeProfitRequestException;
}
