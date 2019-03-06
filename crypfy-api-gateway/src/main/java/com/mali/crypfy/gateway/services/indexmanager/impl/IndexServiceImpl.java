package com.mali.crypfy.gateway.services.indexmanager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.IndexException;
import com.mali.crypfy.gateway.services.indexmanager.IndexService;
import com.mali.crypfy.gateway.services.indexmanager.json.IndexPlanJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${spring.services.indexmanager.index.perfomance-by-date}")
    private String perfomanceByDateResource;
    @Value("${spring.services.indexmanager.index.find-first-by-date}")
    private String findFistByDateResource;

    private ObjectMapper mapper = new ObjectMapper();

    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    @Override
    public BigDecimal getPerfomanceByDate(Integer idplan, Date start, Date end) throws IndexException {
        RestTemplate restTemplate = new RestTemplate();

        try {
            start = DateUtils.getDateWithoutHour(start);
            end = DateUtils.getDateWithoutHour(end);
        } catch (ParseException e) {
            //do nothing
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(perfomanceByDateResource)
                .queryParam("idplan",idplan)
                .queryParam("start",dt.format(start))
                .queryParam("end",dt.format(end));

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new IndexException("Ocorreu um erro no servidor",500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new IndexException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } else
            return new BigDecimal(String.valueOf(restResponseJSON.getResponse()));
    }

    @Override
    public IndexPlanJSON getFirstByDate(Integer idplan, Date date) throws IndexException {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(findFistByDateResource)
                .queryParam("idplan",idplan)
                .queryParam("date",dt.format(date));

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new IndexException("Ocorreu um erro no servidor",500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new IndexException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } else
            return mapper.convertValue(restResponseJSON.getResponse(),IndexPlanJSON.class);
    }
}
