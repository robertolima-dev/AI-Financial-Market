package com.mali.crypfy.indexmanager.rest;

import com.mali.crypfy.indexmanager.core.BalancePerDate;
import com.mali.crypfy.indexmanager.core.MonthlyPerfomance;
import com.mali.crypfy.indexmanager.core.PlanService;
import com.mali.crypfy.indexmanager.core.Statistics;
import com.mali.crypfy.indexmanager.core.exception.PlanException;
import com.mali.crypfy.indexmanager.persistence.entity.Plan;
import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;
import com.mali.crypfy.indexmanager.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class PlanRest {

    @Autowired
    private PlanService planService;

    @RequestMapping(value = "/plans",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> list(@RequestParam(required = false) String email, @RequestParam(required = false) UserPlanStatus status) {
        RestResponse restResponse = new RestResponse();
        try {
            List<UserPlan> plans = planService.list(email,status);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(plans);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> add(@RequestBody UserPlan userPlan) {
        RestResponse restResponse = new RestResponse();
        try {
            userPlan = planService.add(userPlan);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setResponse(userPlan);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/in-progress",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getInProgressPlans(@RequestParam("email") String email) {
        RestResponse restResponse = new RestResponse();
        try {
            List<UserPlan> plans = planService.getInProgressPlans(email);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(plans);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/all-evolution-per-point",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getAllBalanceEvolutionPerPoint(@RequestParam("email") String email) {
        RestResponse restResponse = new RestResponse();
        try {
            List<BalancePerDate> balance = planService.getAllBalanceEvolutionPerPoint(email);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(balance);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/total-profit",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getTotalProfit(@RequestParam("email") String email) {
        RestResponse restResponse = new RestResponse();
        try {
            BigDecimal totalProfit = planService.getTotalProfit(email);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(totalProfit);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/count-all",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> countAllByEmail(@RequestParam("email") String email) {
        RestResponse restResponse = new RestResponse();
        try {
            Long count = planService.countAllByEmail(email);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(count);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    //for admin use
    @RequestMapping(value = "/plans/statistics",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getStatistics() {
        RestResponse restResponse = new RestResponse();
        try {
            Statistics stats = planService.getPlansStats();
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(stats);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/{idplan}/perfomance-by-year",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getPlanByStatus(@PathVariable Integer idplan) {
        RestResponse restResponse = new RestResponse();
        try {
            Date start = DateUtils.addMonthFromNow(-12);
            start = DateUtils.setFirstDay(start);
            Date end = new Date();

            List<MonthlyPerfomance> monthlyPerfomances = planService.getPerfomancePlanMonthly(idplan,start,end);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(monthlyPerfomances);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/{iduserPlan}/change-status-to-in-progress",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> changePlanStatusToInProgress(@PathVariable Integer iduserPlan) {
        RestResponse restResponse = new RestResponse();
        try {
            UserPlan userPlan = planService.changeStatusToInProgress(iduserPlan);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(null);
            return new ResponseEntity<RestResponse>(restResponse, HttpStatus.OK);
        } catch (PlanException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setMessage("Ocorreu um erro no servidor");
            return new ResponseEntity<RestResponse>(restResponse,HttpStatus.OK);
        }
    }
}
