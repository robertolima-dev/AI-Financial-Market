package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.moneymanager.BankAccountService;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankAccountException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankException;
import com.mali.crypfy.gateway.services.moneymanager.json.BankAccountJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.BankJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountRest {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;

    @RequestMapping(value = "/bank-accounts/crypfy-accounts" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> listCrypfyAccounts() {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<BankAccountJSON> bankAccountsJSON = bankAccountService.list("no-reply@crypfy.com");
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(bankAccountsJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/bank-accounts" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> list(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            List<BankAccountJSON> bankAccountsJSON = bankAccountService.list(userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(bankAccountsJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/bank-accounts" ,method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> add(@RequestHeader(value="Authorization") String token, @RequestBody BankAccountJSON bankAccountJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            bankAccountJSON.setEmail(userJSON.getEmail());
            bankAccountJSON = bankAccountService.add(bankAccountJSON);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setMessage("Conta Bancária cadastrada com sucesso!");
            restResponseJSON.setStatus(201);
            restResponseJSON.setResponse(bankAccountJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.CREATED);
        } catch (BankAccountException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value="/bank-accounts/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponseJSON> delete(@RequestHeader(value="Authorization") String token, @PathVariable Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            bankAccountService.delete(id,userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Conta Bancária Excluida com sucesso!");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/bank-accounts/{id}" ,method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> update(@RequestHeader(value="Authorization") String token, @PathVariable Integer id,@RequestBody BankAccountJSON bankAccountJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            bankAccountJSON.setEmail(userJSON.getEmail());
            bankAccountJSON.setIdbankAccount(id);
            bankAccountJSON = bankAccountService.update(bankAccountJSON);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Conta Bancária atualizada com sucesso!");
            restResponseJSON.setResponse(bankAccountJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/bank-accounts/{id}" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> find(@RequestHeader(value="Authorization") String token,@PathVariable Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            BankAccountJSON bankAccountJSON = bankAccountService.find(id, userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(bankAccountJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/banks" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> listBanks() {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<BankJSON> bankAccountsJSON = bankAccountService.listBanks();
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(bankAccountsJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (BankException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }


}
