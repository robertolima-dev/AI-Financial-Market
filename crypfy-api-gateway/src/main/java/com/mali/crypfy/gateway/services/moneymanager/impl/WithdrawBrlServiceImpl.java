package com.mali.crypfy.gateway.services.moneymanager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.moneymanager.WithdrawBrlService;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.WithdrawBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WithdrawBrlServiceImpl implements WithdrawBrlService {

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${spring.services.moneymanager.withdraw.add}")
    private String withdrawAddResource;
    @Value("${spring.services.moneymanager.withdraw.change-status-to-processing}")
    private String changewithdrawStatusToProcessingResource;
    @Value("${spring.services.moneymanager.withdraw.change-status-to-confirmed}")
    private String changewithdrawStatusToConfirmedResource;
    @Value("${spring.services.moneymanager.withdraw.change-status-to-denied}")
    private String changewithdrawStatusToDeniedResource;
    @Value("${spring.services.moneymanager.withdraw.change-status-to-cancelled}")
    private String changewithdrawStatusToCancelledResource;
    @Value("${spring.services.moneymanager.withdraw.list}")
    private String withdrawListResource;
    @Value("${spring.services.moneymanager.withdraw.done-sum-amount}")
    private String withdrawDoneSumAmountResource;
    @Value("${spring.services.moneymanager.withdraw.count}")
    private String countResource;
    @Value("${spring.services.moneymanager.withdraw.delete}")
    private String withdrawDeleteResource;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public DepositWithdrawRequestlBrlJSON addWithdraw(DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON) throws WithdrawBrlException {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DepositWithdrawRequestlBrlJSON> request = new HttpEntity<>(depositWithdrawRequestlBrlJSON);
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(withdrawAddResource, HttpMethod.POST,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);

    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToProcessing(Integer iddepositwithdrawRequestBrl) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changewithdrawStatusToProcessingResource.replace("{id}",String.valueOf(iddepositwithdrawRequestBrl));

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToConfirmed(Integer iddepositwithdrawRequestBrl) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changewithdrawStatusToConfirmedResource.replace("{id}",String.valueOf(iddepositwithdrawRequestBrl));

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON  changeWithdrawStatusToDenied(Integer iddepositwithdrawRequestBrl, String deniedReason) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changewithdrawStatusToDeniedResource.replace("{id}",String.valueOf(iddepositwithdrawRequestBrl));

        Map<String,String> body = new HashMap<String,String>();
        body.put("deniedReason",deniedReason);
        HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(body ,headers);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToCancelled(Integer iddepositwithdrawRequestBrl, String email) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changewithdrawStatusToCancelledResource.replace("{id}",String.valueOf(iddepositwithdrawRequestBrl));
        resource = resource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public List<DepositWithdrawRequestlBrlJSON> list(String email,String status) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(withdrawListResource)
                .queryParam("email",email)
                .queryParam("status",status);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(builder.build().encode().toUri(),HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return (List<DepositWithdrawRequestlBrlJSON>) restResponseJSON.getResponse();
    }

    @Override
    public void delete(Integer iddepositWithdrawRequestBrl, String email) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = withdrawDeleteResource.replace("{id}",String.valueOf(iddepositWithdrawRequestBrl));
        resource = resource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.DELETE,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public BigDecimal getDoneSumAmount(String email) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(withdrawDoneSumAmountResource+email,HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new WithdrawBrlException("Ocorreu um erro no servidor",null,500);

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return (restResponseJSON.getResponse() != null) ? new BigDecimal(String.valueOf(restResponseJSON.getResponse())) : BigDecimal.ZERO;
    }

    @Override
    public Long count(String email, String status) throws WithdrawBrlException {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(countResource);

        if(email != null && !email.equals(""))
            builder.queryParam("email",email);
        if(status != null && !status.equals(""))
            builder.queryParam("status",status);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(builder.build().encode().toUri().toString(),HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new WithdrawBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new WithdrawBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return new Long(restResponseJSON.getResponse().toString());
    }
}
