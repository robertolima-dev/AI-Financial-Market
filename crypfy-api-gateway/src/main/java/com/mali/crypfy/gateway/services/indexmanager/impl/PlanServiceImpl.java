package com.mali.crypfy.gateway.services.indexmanager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseListJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.indexmanager.PlanService;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.indexmanager.json.BalancePerDateJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.StatisticsJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.MonthlyPerfomanceJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankAccountException;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;
import com.mali.crypfy.gateway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PlanServiceImpl implements PlanService {

    @Value("${spring.services.indexmanager.plan.list-in-progress}")
    private String listInProgressResource;
    @Value("${spring.services.indexmanager.plan.list}")
    private String listResource;
    @Value("${spring.services.indexmanager.plan.all-evolution-per-point}")
    private String allEvolutionPerPointResource;
    @Value("${spring.services.indexmanager.plan.count-all}")
    private String countAllResource;
    @Value("${spring.services.indexmanager.plan.total-profit}")
    private String totalProfitResource;
    @Value("${spring.services.indexmanager.plan.perfomance-year}")
    private String perfomanceYearResource;
    @Value("${spring.services.indexmanager.plan.add}")
    private String addResource;
    @Value("${spring.services.indexmanager.plan.statistics}")
    private String adminStatisticsResource;
    @Value("${spring.services.indexmanager.plan.change-status-to-in-progress}")
    private String changeStatusToInProgressService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserPlanJSON add(UserPlanJSON userPlanJSON) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserPlanJSON> request = new HttpEntity<>(userPlanJSON);
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(addResource, HttpMethod.POST,request,RestResponseJSON.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseJSON restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess())
            return mapper.convertValue(restResponseJSON.getResponse(),UserPlanJSON.class);
        else {
            List<ServiceItemError> errors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new PlanException(restResponseJSON.getMessage(),errors,restResponseJSON.getStatus());
        }
    }

    @Override
    public List<UserPlanJSON> getInProgressPlans(String email) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseListJSON<UserPlanJSON>> response = restTemplate.exchange(listInProgressResource+email, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<UserPlanJSON>>() {});

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseListJSON<UserPlanJSON> restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<UserPlanJSON>) restResponseJSON.getResponse();
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    @Override
    public List<UserPlanJSON> list(String email,String status) throws PlanException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(listResource);

        if(email != null && !email.equals(""))
            builder.queryParam("email",email);
        if(status != null && !status.equals(""))
            builder.queryParam("status",status);

        String uri = builder.build().encode().toUri().toString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseListJSON<UserPlanJSON>> response = restTemplate.exchange(uri, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<UserPlanJSON>>() {});

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseListJSON<UserPlanJSON> restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<UserPlanJSON>) restResponseJSON.getResponse();
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    @Override
    public List<BalancePerDateJSON> getAllBalanceEvolutionPerPoint(String email) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(allEvolutionPerPointResource+email, HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseJSON restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<BalancePerDateJSON>) restResponseJSON.getResponse();
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    @Override
    public BigDecimal getTotalProfit(String email) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(totalProfitResource+email, HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseJSON restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return new BigDecimal(String.valueOf(restResponseJSON.getResponse()));
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    @Override
    public Long countAll(String email) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(countAllResource+email, HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseJSON restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return new Long(String.valueOf(restResponseJSON.getResponse()));
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    @Override
    public StatisticsJSON getPlansStats() throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(adminStatisticsResource, HttpMethod.GET,null,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return mapper.convertValue(restResponseJSON.getResponse(),StatisticsJSON.class);
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    @Override
    public UserPlanJSON changeStatusToInProgress(Integer iduserPlan) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();
        String uri = changeStatusToInProgressService.replace("{iduserPlan}",iduserPlan.toString());
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(uri, HttpMethod.PUT,null,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return mapper.convertValue(restResponseJSON.getResponse(),UserPlanJSON.class);
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }

    public List<MonthlyPerfomanceJSON> getPerfomanceYear(Integer idplan) throws PlanException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = perfomanceYearResource.replace("{id}",idplan.toString());
        ResponseEntity<RestResponseListJSON<MonthlyPerfomanceJSON>> response = restTemplate.exchange(resource, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<MonthlyPerfomanceJSON>>() {});

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new PlanException("Ocorreu um erro no servidor",500);

        RestResponseListJSON<MonthlyPerfomanceJSON> restResponseJSON = response.getBody();
        if(restResponseJSON.isSuccess())
            return (List<MonthlyPerfomanceJSON>) restResponseJSON.getResponse();
        else
            throw new PlanException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
    }
}
