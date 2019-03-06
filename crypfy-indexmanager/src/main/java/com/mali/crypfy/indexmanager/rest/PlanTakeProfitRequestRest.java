package com.mali.crypfy.indexmanager.rest;

import com.mali.crypfy.indexmanager.core.PlanRequestProfitService;
import com.mali.crypfy.indexmanager.core.exception.AccountNotVerifiedException;
import com.mali.crypfy.indexmanager.core.exception.PlanRequestProfitAddAllException;
import com.mali.crypfy.indexmanager.core.exception.PlanRequestProfitException;
import com.mali.crypfy.indexmanager.persistence.entity.PlanTakeProfitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class PlanTakeProfitRequestRest {

    @Autowired
    private PlanRequestProfitService planRequestProfitService;

    @RequestMapping(name = "/plan-take-profit-requests",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> add(@RequestBody List<PlanTakeProfitRequest> planTakeProfitRequests) {
        RestResponse restResponse = new RestResponse();
        try {
            planRequestProfitService.addAll(planTakeProfitRequests);
            restResponse.setMessage("Intenção de Movimentação de lucro salva com sucesso, Estado atual 'Aguardando Processamento'");
            restResponse.setResponse(null);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.CREATED);
        } catch (PlanRequestProfitAddAllException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (AccountNotVerifiedException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(name = "/plan-take-profit-requests",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> list(@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            List<PlanTakeProfitRequest> planTakeProfitRequests = planRequestProfitService.list(email);
            restResponse.setMessage("");
            restResponse.setResponse(planTakeProfitRequests);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanRequestProfitException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> delete(@PathVariable Integer id,@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            planRequestProfitService.delete(email,id);
            restResponse.setMessage("Movimentação de lucro excluída com sucesso!");
            restResponse.setResponse(null);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanRequestProfitException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}/{email}/change-status-to-cancelled",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeStatusToCancelled(@PathVariable Integer id,@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitService.changeStatusToCancelled(email,id);
            planTakeProfitRequest.setUserPlan(null);
            restResponse.setMessage("Movimentação de lucro cancelada com sucesso!");
            restResponse.setResponse(planTakeProfitRequest);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanRequestProfitException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}/{email}/change-status-to-failed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeStatusToFailed(@PathVariable Integer id,@PathVariable String email,@RequestBody Map<String,String> body) {
        RestResponse restResponse = new RestResponse();
        try {
            String failedReason = body.get("failedReason");
            PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitService.changeStatusToFailed(email,id,failedReason);
            planTakeProfitRequest.setUserPlan(null);
            restResponse.setMessage("Movimentação de lucro atualizada com sucesso!");
            restResponse.setResponse(planTakeProfitRequest);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanRequestProfitException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}/{email}/change-status-to-processing",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeStatusToProcessing(@PathVariable Integer id,@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitService.changeStatusToProcessing(email,id);
            planTakeProfitRequest.setUserPlan(null);
            restResponse.setMessage("Movimentação de lucro atualizada com sucesso!");
            restResponse.setResponse(planTakeProfitRequest);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanRequestProfitException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}/{email}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeStatusToConfirmed(@PathVariable Integer id, @PathVariable String email,@RequestBody Map<String,Date> body) {
        RestResponse restResponse = new RestResponse();
        try {
            Date indexDate = body.get("indexDate");
            PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitService.changeStatusToConfirmed(email,id,indexDate);
            planTakeProfitRequest.setUserPlan(null);
            restResponse.setMessage("Movimentação de lucro confirmada com sucesso!");
            restResponse.setResponse(planTakeProfitRequest);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanRequestProfitException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }
}
