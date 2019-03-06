package com.mali.crypfy.moneymanager.rest;

import com.mali.crypfy.moneymanager.core.WithdrawBrlService;
import com.mali.crypfy.moneymanager.core.exception.WithdrawBrlException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class WithdrawRest {

    @Autowired
    private WithdrawBrlService withdrawBrlService;

    @RequestMapping(value = "withdraws",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> add(@RequestBody DepositWithdrawRequestBrl depositWithdrawRequestBrl) {
        RestResponse restResponse = new RestResponse();
        try {
            depositWithdrawRequestBrl = this.withdrawBrlService.add(depositWithdrawRequestBrl);
            restResponse.setSuccess(true);
            restResponse.setStatus(201);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.CREATED);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "withdraws/{id}/change-status-to-processing",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeWithdrawlStatusToProcessing(@PathVariable Integer id) {
        RestResponse restResponse = new RestResponse();
        try {
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = withdrawBrlService.changeToProcessing(id);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "withdraws/{id}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeWithdrawlStatusToConfirmed(@PathVariable Integer id) {
        RestResponse restResponse = new RestResponse();
        try {
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = withdrawBrlService.changeToConfirmed(id);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "withdraws/{id}/{email:.+}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> delete(@PathVariable Integer id,@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            withdrawBrlService.delete(id,email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setMessage("Intenção de saque excluída com sucesso!");
            restResponse.setResponse(null);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setStatus(e.getStatus());
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "withdraws/{id}/change-status-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeWithdrawlStatusToDenied(@PathVariable Integer id, @RequestBody Map<String,String> body) {
        RestResponse restResponse = new RestResponse();
        try {
            String deniedReason = body.get("deniedReason");
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = withdrawBrlService.changeToDenied(id,deniedReason);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "withdraws/{id}/{email}/change-status-to-cancelled",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeWithdrawlStatusToCancelled(@PathVariable Integer id, @PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = withdrawBrlService.changeToCancelled(id,email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "withdraws",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> list(@RequestParam(required = false) String email,@RequestParam(required = false)  StatusDepositWithdrawBrl status) {
        RestResponse restResponse = new RestResponse();
        try {
            List<DepositWithdrawRequestBrl> deposits = withdrawBrlService.list(email,status);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(deposits);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/withdraws/done-sum-amount",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getDoneSumAmount(@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            BigDecimal sumAmount = withdrawBrlService.sumAmountConfirmed(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(sumAmount);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/withdraws/count",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> count(@RequestParam String email, @RequestParam StatusDepositWithdrawBrl status) {
        RestResponse restResponse = new RestResponse();
        try {
            Long count = withdrawBrlService.count(email,status);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(count);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

}
