package com.mali.crypfy.moneymanager.integration.auth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;
import com.mali.crypfy.moneymanager.integration.auth.User;
import com.mali.crypfy.moneymanager.integration.auth.UserService;
import com.mali.crypfy.moneymanager.integration.auth.exception.UserException;
import com.mali.crypfy.moneymanager.rest.RestResponse;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * User Service Implementation
 */
@Service
public class UserServiceImpl implements UserService {

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${app.integrations.rest.user.get-info}")
    private String getInfoResource;
    @Value("${app.integrations.rest.user.is-account-verified}")
    private String isAccountVerifiedResource;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public BigDecimal getAvailableBalanceBrl(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();
        String resource = getInfoResource.replace("{email}",email);
        ResponseEntity<RestResponse> response = restTemplate.exchange(resource,HttpMethod.GET,null,RestResponse.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("error on consumes user api", errors);
        }

        RestResponse restResponseJSON = response.getBody();

        Map<String,Object> user = (Map<String, Object>) restResponseJSON.getResponse();

        return new BigDecimal(user.get("availableBalanceBrl").toString());
    }

    @Override
    public User getInfo(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();
        String resource = getInfoResource.replace("{email}",email);
        ResponseEntity<RestResponse> response = restTemplate.exchange(resource,HttpMethod.GET,null,RestResponse.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("error on consumes user api", errors);
        }

        RestResponse restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess())
            return mapper.convertValue(restResponseJSON.getResponse(),User.class);
        else {
            throw new UserException(restResponseJSON.getMessage());
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
