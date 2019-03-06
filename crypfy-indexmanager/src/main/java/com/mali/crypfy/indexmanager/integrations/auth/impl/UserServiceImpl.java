package com.mali.crypfy.indexmanager.integrations.auth.impl;

import com.mali.crypfy.indexmanager.core.exception.ServiceItemError;
import com.mali.crypfy.indexmanager.integrations.auth.UserService;
import com.mali.crypfy.indexmanager.integrations.auth.exceptions.UserException;
import com.mali.crypfy.indexmanager.rest.RestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${spring.services.auth.get-info}")
    private String getInfoResource;
    @Value("${spring.services.auth.add-available-balance-brl}")
    private String addAvailableBalanceBrlResource;
    @Value("${spring.services.auth.is-account-verified}")
    private String isAccountVerifiedResource;

    @Override
    public BigDecimal getAvaiableBalance(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = getInfoResource.replace("{email}",email);

        ResponseEntity<RestResponse> response = restTemplate.exchange(resource, HttpMethod.GET,null,RestResponse.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new UserException("Ocorreu um erro no servidor");

        RestResponse restResponse = response.getBody();

        if(restResponse.isSuccess()) {
            Map<String,Object> user = (Map<String, Object>) restResponse.getResponse();
            return new BigDecimal(user.get("availableBalanceBrl").toString());
        } else {
            throw new UserException(restResponse.getMessage());
        }
    }

    @Override
    public BigDecimal addAvailableBalance(String email, BigDecimal amount) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        Map<String,Double> params = new HashMap<>();
        params.put("amount",amount.doubleValue());

        HttpEntity< Map<String,Double>> request = new HttpEntity<>(params);
        ResponseEntity<RestResponse> response = restTemplate.exchange(addAvailableBalanceBrlResource.replace("{email}",email), HttpMethod.PUT, request, RestResponse.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponse restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException("Ocorreu um erro no servidor", serviceItemErrors,restResponseJSON.getStatus());
        } else {
            return new BigDecimal(restResponseJSON.getResponse().toString());
        }
    }

    @Override
    public boolean isAccountVerified(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = isAccountVerifiedResource.replace("{email}",email);

        ResponseEntity<RestResponse> response = restTemplate.exchange(resource, HttpMethod.GET,null,RestResponse.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new UserException("Ocorreu um erro no servidor");

        RestResponse restResponse = response.getBody();

        if(restResponse.isSuccess()) {
            boolean isAccountVerified= (boolean) restResponse.getResponse();
            return isAccountVerified;
        } else {
            throw new UserException(restResponse.getMessage());
        }
    }
}
