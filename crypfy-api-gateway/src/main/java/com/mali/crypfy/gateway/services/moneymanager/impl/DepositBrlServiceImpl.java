package com.mali.crypfy.gateway.services.moneymanager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.ChangeDepositStatusToConfirmedJSON;
import com.mali.crypfy.gateway.rest.json.ChangeDepositStatusToDeniedJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseListJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.moneymanager.DepositBrlService;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.WithdrawBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.ChangeDepositStatusToCancelledJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.ChangeDepositStatusToWaitingApprovalJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DepositBrlServiceImpl implements DepositBrlService {

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${spring.services.moneymanager.deposit.add}")
    private String depositAddResource;
    @Value("${spring.services.moneymanager.deposit.change-status-to-waiting-approval}")
    private String depositChangeToWaitingApprovalResource;
    @Value("${spring.services.moneymanager.deposit.change-status-to-confirmed}")
    private String depositChangeToConfirmedResource;
    @Value("${spring.services.moneymanager.deposit.change-status-to-denied}")
    private String depositChangeToDeniedResource;
    @Value("${spring.services.moneymanager.deposit.change-status-to-cancelled}")
    private String depositChangeToCancelledResource;
    @Value("${spring.services.moneymanager.deposit.list}")
    private String depositListResource;
    @Value("${spring.services.moneymanager.deposit.done-sum-amount}")
    private String depositDoneSumAmount;
    @Value("${spring.services.moneymanager.deposit.count}")
    private String countResource;
    @Value("${spring.services.moneymanager.deposit.delete}")
    private String depositDeleteResource;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public DepositWithdrawRequestlBrlJSON addDeposit(DepositWithdrawRequestlBrlJSON depositWithdrawlRequestlBrl) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<DepositWithdrawRequestlBrlJSON> request = new HttpEntity<>(depositWithdrawlRequestlBrl);
        ResponseEntity<RestResponseJSON> response =  restTemplate.exchange(depositAddResource,HttpMethod.POST,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToWaitingApproval(Integer iddepositWithdrawlRequestBrl, String email, String photo) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = depositChangeToWaitingApprovalResource.replace("{id}",iddepositWithdrawlRequestBrl.toString()).replace("{email}",email);

        HttpEntity<String> request = new HttpEntity<String>(photo,headers);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            //convert linkedhashmap to object
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToConfirmed(Integer iddepositWithdrawlRequestBrl) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = depositChangeToConfirmedResource.replace("{id}",iddepositWithdrawlRequestBrl.toString());
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToDenied(Integer iddepositWithdrawlRequestBrl, String deniedReason) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = depositChangeToDeniedResource.replace("{id}",iddepositWithdrawlRequestBrl.toString());

        Map<String,String> body = new HashMap<String,String>();
        body.put("deniedReason",deniedReason);
        HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(body ,headers);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToCancelled(Integer iddepositWithdrawlRequestBrl, String email) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = depositChangeToCancelledResource.replace("{id}",iddepositWithdrawlRequestBrl.toString());
        resource = resource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),DepositWithdrawRequestlBrlJSON.class);
    }

    @Override
    public void delete(Integer iddepositWithdrawRequestBrl, String email) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = depositDeleteResource.replace("{id}",String.valueOf(iddepositWithdrawRequestBrl));
        resource = resource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.DELETE,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public List<DepositWithdrawRequestlBrlJSON> list(String email, String status) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(depositListResource)
                .queryParam("email",email)
                .queryParam("status",status);

        ResponseEntity<RestResponseListJSON<DepositWithdrawRequestlBrlJSON>> response = restTemplate.exchange(builder.build().encode().toUri().toString(),HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<DepositWithdrawRequestlBrlJSON>>() {});

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseListJSON<DepositWithdrawRequestlBrlJSON> restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return (List<DepositWithdrawRequestlBrlJSON>) restResponseJSON.getResponse();

    }

    @Override
    public BigDecimal getDoneSumAmount(String email) throws DepositBrlException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(depositDoneSumAmount+email,HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new DepositBrlException("Ocorreu um erro no servidor",null,500);

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return (restResponseJSON.getResponse() != null) ? new BigDecimal(String.valueOf(restResponseJSON.getResponse())) : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getMonthSumAmount(List<DepositWithdrawRequestlBrlJSON> deposits) throws DepositBrlException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));
        Date dayOne = calendar.getTime();
        BigDecimal sum = BigDecimal.ZERO;

        for (DepositWithdrawRequestlBrlJSON deposit: deposits){
            if(deposit.getCreated().getTime()>dayOne.getTime()){
                sum = sum.add(deposit.getAmount());
            }
        }

        return sum;
    }

    @Override
    public Long count(String email, String status) throws DepositBrlException {
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
            throw new DepositBrlException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new DepositBrlException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else
            return new Long(restResponseJSON.getResponse().toString());
    }
}
