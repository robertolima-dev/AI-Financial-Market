package com.mali.crypfy.moneymanager.rest;

import com.mali.crypfy.moneymanager.core.BankAccountService;
import com.mali.crypfy.moneymanager.core.exception.BankAccountException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.persistence.entity.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountRest {

    @Autowired
    private BankAccountService bankAccountService;

    @RequestMapping(value = "bank-accounts",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> add(@RequestBody BankAccount bankAccount) {
        RestResponse restResponse = new RestResponse();
        try {
            bankAccount = bankAccountService.add(bankAccount);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setMessage("Conta Bancária criada com sucesso!");
            restResponse.setResponse(bankAccount);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.CREATED);
        } catch (BankAccountException e) {
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "bank-accounts/{id}",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> find(@PathVariable Integer id, @RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            BankAccount bankAccount = bankAccountService.findByIdAndEmail(email,id);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("");
            restResponse.setResponse(bankAccount);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (NoResultException e) {
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(404);
            restResponse.setResponse(null);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "bank-accounts/{id}",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> update(@PathVariable Integer id,@RequestBody BankAccount bankAccount) {
        RestResponse restResponse = new RestResponse();
        try {
            bankAccount.setIdbankAccount(id);
            bankAccount = bankAccountService.update(bankAccount);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("Conta Bancária atualizada com sucesso!");
            restResponse.setResponse(bankAccount);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(name = "bank-accounts/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> update(@PathVariable Integer id,@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            bankAccountService.delete(email,id);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("Conta Bancária excluída com sucesso!");
            restResponse.setResponse(null);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "bank-accounts/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> delete(@PathVariable Integer id,@RequestParam("email") String email) {
        RestResponse restResponse = new RestResponse();
        try {
            bankAccountService.delete(email,id);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("Conta Bancária excluída com sucesso!");
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponse.setSuccess(false);
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "bank-accounts",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> list(@RequestParam String email) {
        RestResponse restResponse = new RestResponse();
        try {
            List<BankAccount> bankAccounts = bankAccountService.list(email);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(bankAccounts);
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        } catch (BankAccountException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }
}
