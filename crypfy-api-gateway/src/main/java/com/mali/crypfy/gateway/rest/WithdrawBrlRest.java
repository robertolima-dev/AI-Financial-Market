package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.DepositWithdrawBrlChangeStatusToDenied;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.moneymanager.WithdrawBrlService;
import com.mali.crypfy.gateway.services.moneymanager.enumeration.TypeDepositWithdraw;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.WithdrawBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.*;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import com.mali.crypfy.gateway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class WithdrawBrlRest {

    @Autowired
    private WithdrawBrlService withdrawBrlService;
    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;

    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @RequestMapping(value = "/withdraws-brl",method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> addWithdrawl(@RequestHeader(value="Authorization") String token,@RequestBody DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            depositWithdrawRequestlBrlJSON.setEmail(userJSON.getEmail());
            depositWithdrawRequestlBrlJSON = withdrawBrlService.addWithdraw(depositWithdrawRequestlBrlJSON);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setResponse(depositWithdrawRequestlBrlJSON);
            restResponseJSON.setMessage("Intenção de saque cadastrada com sucesso!");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.CREATED);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/admin/withdraws-brl/{id}/change-status-to-processing",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToProcessing(@PathVariable String email,@PathVariable Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON = withdrawBrlService.changeWithdrawStatusToProcessing(id);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Status atualizado com sucesso!");
            restResponseJSON.setResponse(depositWithdrawRequestlBrlJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/withdraws-brl/{id}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToConfirmed(@PathVariable String email,@PathVariable Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON = withdrawBrlService.changeWithdrawStatusToConfirmed(id);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Status atualizado com sucesso!");
            restResponseJSON.setResponse(depositWithdrawRequestlBrlJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/withdraws-brl/{id}/change-status-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToDenied(@PathVariable String email, @PathVariable Integer id,@RequestBody DepositWithdrawBrlChangeStatusToDenied depositWithdrawBrlChangeStatusToDenied) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON = withdrawBrlService.changeWithdrawStatusToDenied(id,depositWithdrawBrlChangeStatusToDenied.getDeniedReason());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Status atualizado com sucesso!");
            restResponseJSON.setResponse(depositWithdrawRequestlBrlJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/withdraws-brl/{id}/change-status-to-cancelled",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToCancelled(@RequestHeader(value="Authorization") String token,@PathVariable Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON = withdrawBrlService.changeWithdrawStatusToCancelled(id,userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Status atualizado com sucesso!");
            restResponseJSON.setResponse(depositWithdrawRequestlBrlJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "withdraws-brl/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponseJSON> delete(@RequestHeader(value="Authorization") String token,@PathVariable  Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            withdrawBrlService.delete(id,userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Intenção de saque excluída com sucesso!");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/withdraws-brl",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> list(@RequestHeader(value="Authorization") String token, @RequestParam(required = false) String status) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            List<DepositWithdrawRequestlBrlJSON> withdrawls = withdrawBrlService.list(userJSON.getEmail(),status);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(withdrawls);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setStatus(400);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setStatus(e.getStatus());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/admin/withdraws-brl",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> listAdmin(@RequestParam(required = false) String status,@RequestParam(required = false) String email) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<DepositWithdrawRequestlBrlJSON> withdrawls = withdrawBrlService.list(email,status);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(withdrawls);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setStatus(400);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setStatus(e.getStatus());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/withdraws-brl/{id}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> secretChangeStatusToConfirmed(@RequestParam String secret,@PathVariable Integer id) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(401);
            restResponseJSON.setMessage("Not Authorized");
            restResponseJSON.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }

        try {
            withdrawBrlService.changeWithdrawStatusToConfirmed(id);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Saque confirmado com sucesso");
            restResponseJSON.setResponse("Saque confirmado com sucesso");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/withdraws-brl/{id}/change-status-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> secretChangeStatusToDenied(@RequestParam String secret,@PathVariable Integer id,@RequestBody DepositWithdrawBrlChangeStatusToDenied depositWithdrawBrlChangeStatusToDenied) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(401);
            restResponseJSON.setMessage("Not Authorized");
            restResponseJSON.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }

        try {
            withdrawBrlService.changeWithdrawStatusToDenied(id,depositWithdrawBrlChangeStatusToDenied.getDeniedReason());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Saque negado com sucesso");
            restResponseJSON.setResponse("Saque negado com sucesso");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/withdraws-brl/{id}/change-status-to-processing",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> secretChangeStatusToProcessing(@RequestParam String secret,@PathVariable Integer id) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(401);
            restResponseJSON.setMessage("Not Authorized");
            restResponseJSON.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }

        try {
            withdrawBrlService.changeWithdrawStatusToProcessing(id);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Saque atualizado como processando com sucesso");
            restResponseJSON.setResponse("Saque atualizado como processando com sucesso");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (WithdrawBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }
}
