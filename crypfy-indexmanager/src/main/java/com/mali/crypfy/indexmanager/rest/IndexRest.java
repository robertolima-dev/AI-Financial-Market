package com.mali.crypfy.indexmanager.rest;

import com.mali.crypfy.indexmanager.core.IndexService;
import com.mali.crypfy.indexmanager.core.exception.PlanException;
import com.mali.crypfy.indexmanager.persistence.entity.IndexPlan;
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
import java.util.List;

@RestController
public class IndexRest {

    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/indexes",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> list(@RequestParam("idplan") Integer idplan) {
        RestResponse restResponse = new RestResponse();

        try {
            List<IndexPlan> indexes = indexService.getIndexesByIdPlan(idplan);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(indexes);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (Exception e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/indexes/performance-by-date",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getPerformance(@RequestParam("idplan") Integer idplan, @RequestParam("start") @DateTimeFormat(pattern ="yyyy-MM-dd-HH:mm:ss") Date start, @RequestParam("end") @DateTimeFormat(pattern ="yyyy-MM-dd-HH:mm:ss") Date end) {
        RestResponse restResponse = new RestResponse();
        try {
            BigDecimal performance = indexService.getPerformanceBetweenDates(idplan,start,end);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(performance);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (Exception e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/indexes/first-index-by-date",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getFirstIndexByDate(@RequestParam("idplan") Integer idplan,@RequestParam("date") @DateTimeFormat(pattern ="yyyy-MM-dd") Date date) {
        RestResponse restResponse = new RestResponse();
        try {
            IndexPlan indexPlan = indexService.getFirstIndexByDate(idplan,date);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(indexPlan);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (Exception e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

}
