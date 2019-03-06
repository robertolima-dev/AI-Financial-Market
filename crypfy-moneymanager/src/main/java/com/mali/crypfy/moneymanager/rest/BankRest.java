package com.mali.crypfy.moneymanager.rest;

import com.mali.crypfy.moneymanager.core.BankService;
import com.mali.crypfy.moneymanager.core.exception.BankException;
import com.mali.crypfy.moneymanager.persistence.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankRest {

    @Autowired
    private BankService bankService;

    @RequestMapping(value = "/banks",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findAll() {
        RestResponse restResponse = new RestResponse();
        try {
            List<Bank> banks = this.bankService.list();
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(banks);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (BankException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }
}
