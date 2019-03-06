package com.mali.crypfy.moneymanager.rest;

import com.mali.crypfy.moneymanager.core.DepositBrlService;
import com.mali.crypfy.moneymanager.core.exception.DepositBrlException;
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
public class DepositRest {

    @Autowired
    private DepositBrlService depositBrlService;

    @RequestMapping(value = "deposits",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> add(@RequestBody DepositWithdrawRequestBrl depositWithdrawRequestBrl) {
        RestResponse restResponse = new RestResponse();
        try {
            depositWithdrawRequestBrl = depositBrlService.add(depositWithdrawRequestBrl);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.CREATED);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "deposits/{id}/{email}/change-status-to-waiting-approval",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeDepositStatusToWaitingApproval(@PathVariable Integer id,@PathVariable String email, @RequestBody String photo) {
        RestResponse restResponse = new RestResponse();
        try {
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = depositBrlService.changeToWaitingApproval(id,email,photo);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setStatus(e.getStatus());
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "deposits/{id}/{email:.+}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> delete(@PathVariable Integer id,@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            depositBrlService.delete(id,email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setMessage("Intenção de depósito excluída com sucesso!");
            restResponse.setResponse(null);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setStatus(e.getStatus());
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "deposits/{id}/change-status-to-confirmed", method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeDepositStatusToConfirmed(@PathVariable Integer id) {
        RestResponse restResponse = new RestResponse();
        try {
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = depositBrlService.changeToConfirmed(id);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "deposits/{id}/change-status-to-denied", method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeDepositStatusToDenied(@PathVariable Integer id,@RequestBody Map<String,String> body) {
        RestResponse restResponse = new RestResponse();
        try {
            String deniedReason = body.get("deniedReason");
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = depositBrlService.changeToDenied(id,deniedReason);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "deposits/{id}/{email}/change-status-to-cancelled", method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changeDepositStatusToCancelled(@PathVariable Integer id, @PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            DepositWithdrawRequestBrl depositWithdrawRequestBrl = depositBrlService.changeToCancelled(id,email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(depositWithdrawRequestBrl);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/deposits",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> list(@RequestParam(required = false) String email,@RequestParam(required = false) StatusDepositWithdrawBrl status) {
        RestResponse restResponse = new RestResponse();
        try {

            List<DepositWithdrawRequestBrl> deposits = this.depositBrlService.list(email,status);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(deposits);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/deposits/count",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> count(@RequestParam String email, @RequestParam StatusDepositWithdrawBrl status) {
        RestResponse restResponse = new RestResponse();
        try {
            Long count = this.depositBrlService.count(email,status);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(count);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/deposits/done-sum-amount",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getDoneSumAmount(@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            BigDecimal sumAmount = this.depositBrlService.sumAmountConfirmed(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(sumAmount);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

}
