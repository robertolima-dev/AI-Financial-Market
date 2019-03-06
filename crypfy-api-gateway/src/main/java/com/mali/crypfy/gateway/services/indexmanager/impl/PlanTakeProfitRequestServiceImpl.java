package com.mali.crypfy.gateway.services.indexmanager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseListJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.indexmanager.PlanTakeProfitRequestService;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanTakeProfitRequestException;
import com.mali.crypfy.gateway.services.indexmanager.json.PlanTakeProfitRequestJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PlanTakeProfitRequestServiceImpl implements PlanTakeProfitRequestService {

    @Value("${spring.services.indexmanager.plan-take-profit-request.add}")
    private String addResource;
    @Value("${spring.services.indexmanager.plan-take-profit-request.list}")
    private String listResource;
    @Value("${spring.services.indexmanager.plan-take-profit-request.change-status-to-cancelled}")
    private String changeStatusToCancelledResource;
    @Value("${spring.services.indexmanager.plan-take-profit-request.change-status-to-failed}")
    private String changeStatusToFailedResource;
    @Value("${spring.services.indexmanager.plan-take-profit-request.change-status-to-confirmed}")
    private String changeStatusToConfirmedResource;
    @Value("${spring.services.indexmanager.plan-take-profit-request.change-status-to-processing}")
    private String changeStatusToProcessingResource;
    @Value("${spring.services.indexmanager.plan-take-profit-request.delete}")
    private String deleteResource;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void addAll(List<PlanTakeProfitRequestJSON> planTakeProfitRequestsJSON) throws PlanTakeProfitRequestException {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<PlanTakeProfitRequestJSON>> request = new HttpEntity<>(planTakeProfitRequestsJSON);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(addResource, HttpMethod.POST, request, RestResponseJSON.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanTakeProfitRequestException("Ocorreu um erro no servidor", 500);

        RestResponseJSON restResponseJSON = response.getBody();

        if (!restResponseJSON.isSuccess()) {
            Map<String,List<ServiceItemError>> errors = (Map<String,List<ServiceItemError>>) restResponseJSON.getResponse();
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), errors, restResponseJSON.getStatus());
        }
    }

    @Override
    public List<PlanTakeProfitRequestJSON> list(String email) throws PlanTakeProfitRequestException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseListJSON<PlanTakeProfitRequestJSON>> response = restTemplate.exchange(listResource + "?email=" + email, HttpMethod.GET, null, new ParameterizedTypeReference<RestResponseListJSON<PlanTakeProfitRequestJSON>>() {
        });

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanTakeProfitRequestException("Ocorreu um erro no servidor", 500);

        RestResponseListJSON<PlanTakeProfitRequestJSON> restResponseJSON = response.getBody();
        if (restResponseJSON.isSuccess())
            return (List<PlanTakeProfitRequestJSON>) restResponseJSON.getResponse();
        else
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), restResponseJSON.getStatus());
    }

    @Override
    public PlanTakeProfitRequestJSON changeStatusToCancelled(String email, Integer id) throws PlanTakeProfitRequestException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeStatusToCancelledResource.replace("{id}", id.toString());
        resource = resource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource, HttpMethod.PUT, null, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            throw new PlanTakeProfitRequestException("Ocorreu um erro no servidor", 500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), serviceItemErrors, restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(), PlanTakeProfitRequestJSON.class);
    }

    @Override
    public PlanTakeProfitRequestJSON changeStatusToFailed(Integer id, String failedReason) throws PlanTakeProfitRequestException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeStatusToFailedResource.replace("{id}", id.toString());

        Map<String,String> body = new HashMap<String,String>();
        body.put("failedReason",failedReason);
        HttpEntity<Map<String,String>> request = new HttpEntity<>(body);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource, HttpMethod.PUT, request, RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();
        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), serviceItemErrors, restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(), PlanTakeProfitRequestJSON.class);
    }

    @Override
    public PlanTakeProfitRequestJSON changeStatusToProcessing(Integer id) throws PlanTakeProfitRequestException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeStatusToProcessingResource.replace("{id}", id.toString());

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource, HttpMethod.PUT, null, RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();
        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), serviceItemErrors, restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(), PlanTakeProfitRequestJSON.class);
    }

    @Override
    public PlanTakeProfitRequestJSON changeStatusToConfirmed(Integer id, Date indexDate) throws PlanTakeProfitRequestException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeStatusToConfirmedResource.replace("{id}", id.toString());

        Map<String,Date> body = new HashMap<String,Date>();
        body.put("indexDate",indexDate);
        HttpEntity<Map<String,Date>> request = new HttpEntity<>(body);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource, HttpMethod.PUT, request, RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();
        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), serviceItemErrors, restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(), PlanTakeProfitRequestJSON.class);
    }

    @Override
    public void delete(String email, Integer id) throws PlanTakeProfitRequestException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = deleteResource.replace("{id}", id.toString());
        resource = resource + "?email=" + email;

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource, HttpMethod.DELETE, null, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            throw new PlanTakeProfitRequestException("Ocorreu um erro no servidor", 500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new PlanTakeProfitRequestException(restResponseJSON.getMessage(), serviceItemErrors, restResponseJSON.getStatus());
        }
    }
}
