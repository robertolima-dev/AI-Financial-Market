package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.DashboardDataJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.TotalBalanceJSON;
import com.mali.crypfy.gateway.services.indexmanager.PlanService;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.UserService;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.exceptions.UserException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class BalanceRest {

    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "balance/data",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getBalances(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        TotalBalanceJSON balance = new TotalBalanceJSON();
        UserJSON user = null;
        try {
            user = jwtAuthBuilder.getInfo(token);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }

        try {
            List<UserPlanJSON> plans = planService.getInProgressPlans(user.getEmail());
            balance.setBlockedBalance(BigDecimal.ZERO);
            plans.forEach((p) -> {
                balance.setBlockedBalance(balance.getBlockedBalance().add(p.getCurrentBalance()));
            });

            user = userService.getInfo(user.getEmail());

            boolean isPendingAccount = ( (user.getIdentityVerificationStatus().equals("UNVERIFIED") || user.getIdentityVerificationStatus().equals("DENIED")) && (user.getDocumentVerificationStatus().equals("UNVERIFIED") || user.getDocumentVerificationStatus().equals("DENIED"))) ? true : false;

            balance.setBitcoinBalance(BigDecimal.ZERO);
            balance.setAvailableBalance(user.getAvailableBalanceBrl());
            balance.setEstimatedTotalBalance(balance.getBlockedBalance().add(user.getAvailableBalanceBrl()));
            balance.setPendingAccount(isPendingAccount);
            restResponseJSON.setStatus(200);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setResponse(balance);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (PlanException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }
}
