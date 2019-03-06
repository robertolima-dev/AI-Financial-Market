package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.IndexException;
import com.mali.crypfy.gateway.services.indexmanager.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
public class IndexRest {

    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "/indexes/perfomance-by-date",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getPerfomanceByDate(@RequestParam("idplan") Integer idplan, @RequestParam("start") @DateTimeFormat(pattern ="yyyy-MM-dd") Date start, @RequestParam("end") @DateTimeFormat(pattern ="yyyy-MM-dd") Date end) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            BigDecimal perfomance = indexService.getPerfomanceByDate(idplan,start,end);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(perfomance);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }  catch (IndexException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }
}
