package com.mali.crypfy.gateway.services.moneymanager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.moneymanager.BankAccountService;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankAccountException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.BankAccountJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.BankJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService{

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${spring.services.moneymanager.bank-account.list}")
    private String bankAccountListResource;
    @Value("${spring.services.moneymanager.bank.list}")
    private String bankListResource;
    @Value("${spring.services.moneymanager.bank-account.find}")
    private String bankAccountFindResource;
    @Value("${spring.services.moneymanager.bank-account.add}")
    private String bankAccountAddResource;
    @Value("${spring.services.moneymanager.bank-account.update}")
    private String bankAccountUpdateResource;
    @Value("${spring.services.moneymanager.bank-account.delete}")
    private String bankAccountDeleteResource;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public BankAccountJSON add(BankAccountJSON bankAccountJSON) throws BankAccountException {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<BankAccountJSON> request = new HttpEntity<>(bankAccountJSON);
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(bankAccountAddResource,HttpMethod.POST,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new BankAccountException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new BankAccountException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),BankAccountJSON.class);
    }

    @Override
    public BankAccountJSON update(BankAccountJSON bankAccountJSON) throws BankAccountException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = bankAccountUpdateResource.replace("{id}",bankAccountJSON.getIdbankAccount().toString());

        HttpEntity<BankAccountJSON> request = new HttpEntity<>(bankAccountJSON);
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new BankAccountException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new BankAccountException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),BankAccountJSON.class);
    }

    @Override
    public void delete(Integer id, String email) throws BankAccountException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = bankAccountDeleteResource.replace("{id}",id.toString());
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(resource+"?email="+email,HttpMethod.DELETE,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new BankAccountException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new BankAccountException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public List<BankAccountJSON> list(String email) throws BankAccountException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(bankAccountListResource+email, HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new BankAccountException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess())
            return (List<BankAccountJSON>) restResponseJSON.getResponse();
        else {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new BankAccountException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public List<BankJSON> listBanks() throws BankException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(bankListResource, HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new BankException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess())
            return (List<BankJSON>) restResponseJSON.getResponse();
        else {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new BankException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public BankAccountJSON find(Integer id, String email) throws BankAccountException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = bankAccountFindResource.replace("{id}",id.toString());
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(resource+"?email="+email, HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new BankAccountException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess())
            return mapper.convertValue(restResponseJSON.getResponse(),BankAccountJSON.class);
        else {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new BankAccountException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        }
    }
}
