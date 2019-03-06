package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.PlanPerfomanceDataJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.indexmanager.PlanService;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.IndexException;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.indexmanager.json.IndexPlanJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.StatisticsJSON;
import com.mali.crypfy.gateway.services.indexmanager.IndexService;
import com.mali.crypfy.gateway.services.indexmanager.json.MonthlyPerfomanceJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.UserService;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.exceptions.UserException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import com.mali.crypfy.gateway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class PlanRest {

    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;
    @Autowired
    private PlanService planService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private UserService userService;

    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @RequestMapping(value = "/plans",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getInProgressPlans(@RequestHeader(value="Authorization") String token,@RequestParam(required = false) String status) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON user = jwtAuthBuilder.getInfo(token);
            List<UserPlanJSON> plans = planService.list(user.getEmail(),status);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(plans);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/admin/plans",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getPlans(@RequestParam("status") String status) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<UserPlanJSON> plans = planService.list(null,status);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(plans);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans",method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> add(@RequestHeader(value="Authorization") String token,@RequestBody UserPlanJSON userPlanJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON user = jwtAuthBuilder.getInfo(token);
            userPlanJSON.setEmail(user.getEmail());
            userPlanJSON = planService.add(userPlanJSON);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Plano contratado com sucesso! Seu plano está em processamento, e dentro de algumas horas estará vigente");
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/perfomance",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getPlansPerfomance() {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {

            List<PlanPerfomanceDataJSON> perfomances = new ArrayList<PlanPerfomanceDataJSON>();

            for(int i = 1;i<=3;i++){
                PlanPerfomanceDataJSON planPerfomanceDataJSON = new PlanPerfomanceDataJSON();

                int currentMonth = DateUtils.getCurrentMonth();
                int currentYear = DateUtils.getCurrentYear();

                //last six months
                Date start = DateUtils.getFirstDayOfMonth(currentMonth-7,currentYear);
                Date firstDayOfCurrentMonth = DateUtils.getFirstDayOfCurrentMonth();
                planPerfomanceDataJSON.setLastSixMonthPerfomance(indexService.getPerfomanceByDate(i,start,firstDayOfCurrentMonth));

                //last three months
                start = DateUtils.getFirstDayOfMonth(currentMonth-4,currentYear);
                planPerfomanceDataJSON.setLastThreeMonthPerfomance(indexService.getPerfomanceByDate(i,start,firstDayOfCurrentMonth));

                //last month
                start = DateUtils.getFirstDayOfMonth(currentMonth-2,currentYear);
                planPerfomanceDataJSON.setLastMonthPerfomance(indexService.getPerfomanceByDate(i,start,firstDayOfCurrentMonth));

                //current month
                planPerfomanceDataJSON.setCurrentMonthPerfomance(indexService.getPerfomanceByDate(i,DateUtils.getFirstDayOfCurrentMonth(),new Date()));

                //year to date
                planPerfomanceDataJSON.setYearToDatePerfomance(indexService.getPerfomanceByDate(i,DateUtils.getFirstDayOfCurrentYear(),new Date()));

                planPerfomanceDataJSON.setIdplan(i);

                perfomances.add(planPerfomanceDataJSON);
            }

            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(perfomances);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (IndexException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/{idplan}/perfomance",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getPerfomance(@RequestHeader(value="Authorization") String token, @PathVariable Integer idplan) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {

            UserJSON user = jwtAuthBuilder.getInfo(token);

            PlanPerfomanceDataJSON planPerfomanceDataJSON = new PlanPerfomanceDataJSON();

            int currentMonth = DateUtils.getCurrentMonth();
            int currentYear = DateUtils.getCurrentYear();

            Date start = DateUtils.getFirstDayOfMonth(currentMonth-4,currentYear);
            Date firstDayOfCurrentMonth = DateUtils.getFirstDayOfCurrentMonth();

            planPerfomanceDataJSON.setLastThreeMonthPerfomance(indexService.getPerfomanceByDate(idplan,start,firstDayOfCurrentMonth));
            planPerfomanceDataJSON.setCurrentMonthPerfomance(indexService.getPerfomanceByDate(idplan,DateUtils.getFirstDayOfCurrentMonth(),new Date()));

            start = DateUtils.getFirstDayOfMonth(currentMonth-2,currentYear);
            planPerfomanceDataJSON.setLastMonthPerfomance(indexService.getPerfomanceByDate(idplan,start,firstDayOfCurrentMonth));

            List<MonthlyPerfomanceJSON> monthlyPerfomancesJSON = planService.getPerfomanceYear(idplan);
            planPerfomanceDataJSON.setMonthlyPerfomance(monthlyPerfomancesJSON);

            UserJSON userJSON = userService.getInfo(user.getEmail());
            planPerfomanceDataJSON.setUserAvailableBalance(userJSON.getAvailableBalanceBrl());

            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(planPerfomanceDataJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (IndexException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/plans/statistics",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getTotalProfit() {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            StatisticsJSON stats = planService.getPlansStats();
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(stats);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plans/{idplan}/perfomance-by-year",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getPerfomanceYear(@PathVariable Integer idplan) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<MonthlyPerfomanceJSON> monthlyPerfomancesJSON = planService.getPerfomanceYear(idplan);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(monthlyPerfomancesJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/plans/{iduserPlan}/change-status-to-in-progress",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToInProgress(@RequestParam String secret,@PathVariable Integer iduserPlan) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(401);
            restResponseJSON.setMessage("Not Authorized");
            restResponseJSON.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }

        try {
            UserPlanJSON userPlanJSON = planService.changeStatusToInProgress(iduserPlan);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(userPlanJSON);
            restResponseJSON.setMessage("Plano atualizado para vigente com sucesso!");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }
}
