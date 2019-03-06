package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.indexmanager.PlanTakeProfitRequestService;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanTakeProfitRequestException;
import com.mali.crypfy.gateway.services.indexmanager.json.PlanTakeProfitRequestJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import com.mali.crypfy.gateway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class PlanTakeProfitRequestRest {

    @Autowired
    private PlanTakeProfitRequestService planTakeProfitRequestService;

    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @RequestMapping(name = "/plan-take-profit-requests",method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> add(@RequestHeader(value="Authorization") String token,@RequestBody List<PlanTakeProfitRequestJSON> planTakeProfitRequestsJSON) {
        RestResponseJSON restResponse = new RestResponseJSON();
        try {
            UserJSON user = jwtAuthBuilder.getInfo(token);
            for(PlanTakeProfitRequestJSON planTakeProfitRequestJSON : planTakeProfitRequestsJSON) {
                planTakeProfitRequestJSON.setEmail(user.getEmail());
            }
            planTakeProfitRequestService.addAll(planTakeProfitRequestsJSON);
            restResponse.setMessage("Intenções de Movimentação de lucro salva com sucesso, Estado atual 'Aguardando Processamento'");
            restResponse.setResponse(null);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.CREATED);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getMultiErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponse.setStatus(401);
            restResponse.setSuccess(false);
            restResponse.setResponse(null);
            restResponse.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> list(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponse = new RestResponseJSON();
        try {
            UserJSON user = jwtAuthBuilder.getInfo(token);
            List<PlanTakeProfitRequestJSON> planTakeProfitRequestJSON = planTakeProfitRequestService.list(user.getEmail());
            restResponse.setMessage("");
            restResponse.setResponse(planTakeProfitRequestJSON);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.OK);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponse.setStatus(401);
            restResponse.setSuccess(false);
            restResponse.setResponse(null);
            restResponse.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}/change-status-to-cancelled",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToCancelled(@RequestHeader(value="Authorization") String token,@PathVariable  Integer id) {
        RestResponseJSON restResponse = new RestResponseJSON();
        try {
            UserJSON user = jwtAuthBuilder.getInfo(token);
            PlanTakeProfitRequestJSON planTakeProfitRequestJSON = planTakeProfitRequestService.changeStatusToCancelled(user.getEmail(),id);
            restResponse.setMessage("Movimentação de lucro cancelada com sucesso!");
            restResponse.setResponse(planTakeProfitRequestJSON);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.OK);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponse.setStatus(401);
            restResponse.setSuccess(false);
            restResponse.setResponse(null);
            restResponse.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/secret/plan-take-profit-requests/{id}/change-status-to-failed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToFailed(@RequestParam String secret,@PathVariable  Integer id, @RequestBody Map<String,String> body) {
        RestResponseJSON restResponse = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponse.setSuccess(false);
            restResponse.setStatus(401);
            restResponse.setMessage("Not Authorized");
            restResponse.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        }

        try {
            String failedReason = body.get("failedReason");
            PlanTakeProfitRequestJSON planTakeProfitRequestJSON = planTakeProfitRequestService.changeStatusToFailed(id,failedReason);
            restResponse.setMessage("Movimentação de lucro atualizada com sucesso!");
            restResponse.setResponse(planTakeProfitRequestJSON);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.OK);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/plan-take-profit-requests/{id}/change-status-to-processing",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToProcessing(@RequestParam String secret,@PathVariable  Integer id) {
        RestResponseJSON restResponse = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponse.setSuccess(false);
            restResponse.setStatus(401);
            restResponse.setMessage("Not Authorized");
            restResponse.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        }

        try {
            PlanTakeProfitRequestJSON planTakeProfitRequestJSON = planTakeProfitRequestService.changeStatusToProcessing(id);
            restResponse.setMessage("Movimentação de lucro atualizada com sucesso!");
            restResponse.setResponse(planTakeProfitRequestJSON);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.OK);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/plan-take-profit-requests/{id}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToConfirmed(@RequestParam String secret,@PathVariable  Integer id,@RequestBody Map<String,String> body) {
        RestResponseJSON restResponse = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponse.setSuccess(false);
            restResponse.setStatus(401);
            restResponse.setMessage("Not Authorized");
            restResponse.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        }

        try {
            Date indexDate = sdf.parse(body.get("indexDate"));
            PlanTakeProfitRequestJSON planTakeProfitRequestJSON = planTakeProfitRequestService.changeStatusToConfirmed(id,indexDate);
            restResponse.setMessage("Movimentação de lucro confirmada com sucesso!");
            restResponse.setResponse(planTakeProfitRequestJSON);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.OK);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        } catch (ParseException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(500);
            restResponse.setResponse(null);
            restResponse.setMessage("Ocorreu um erro ao confirmar a intenção de movimentação de lucro");
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/plan-take-profit-requests/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponseJSON> delete(@RequestHeader(value="Authorization") String token,@PathVariable  Integer id) {
        RestResponseJSON restResponse = new RestResponseJSON();
        try {
            UserJSON user = jwtAuthBuilder.getInfo(token);
            planTakeProfitRequestService.delete(user.getEmail(),id);
            restResponse.setMessage("Movimentação de lucro excluída com sucesso!");
            restResponse.setResponse(null);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.OK);
        } catch (PlanTakeProfitRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponse,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponse.setStatus(401);
            restResponse.setSuccess(false);
            restResponse.setResponse(null);
            restResponse.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}
