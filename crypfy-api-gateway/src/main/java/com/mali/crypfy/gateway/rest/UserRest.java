package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.user.UserService;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.exceptions.UserException;
import com.mali.crypfy.gateway.services.user.exceptions.UserListException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import com.mali.crypfy.gateway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class UserRest {

    @Autowired
    UserService userService;

    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @RequestMapping(value = "/admin/users",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> list(@RequestParam(required = false) String documentStatus, @RequestParam(required = false) String identityStatus, @RequestParam(required = false) String email) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<UserJSON> users = userService.list(documentStatus,identityStatus,email);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(users);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserListException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/users/{email}/change-identity-to-verified",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeIdentityToVerified(@PathVariable("email") String email) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON user = userService.updateIdentityVerificationStatusToVerified(email);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/users/{email}/change-document-to-verified",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeDocumentToVerified(@PathVariable("email") String email) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON user = userService.updateDocumentVerificationStatusToVerified(email);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/users/{email}/change-identity-to-verified",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> temporaryChangeIdentityToVerified(@RequestParam String secret,@PathVariable("email") String email) {
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
            UserJSON user = userService.updateIdentityVerificationStatusToVerified(email);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/users/{email}/change-document-to-verified",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> temporaryChangeDocumentToVerified(@RequestParam String secret,@PathVariable("email") String email) {
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
            UserJSON user = userService.updateDocumentVerificationStatusToVerified(email);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/users/{email}/change-identity-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeIdentityToDenied(@PathVariable("email") String email, @RequestBody Map<String,String> body) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            String deniedReason = body.get("deniedReason");
            UserJSON user = userService.updateIdentityVerificationStatusToDenied(email,deniedReason);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/users/{email}/change-document-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeDocumentToDenied(@PathVariable("email") String email,@RequestBody Map<String,String> body) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            String deniedReason = body.get("deniedReason");
            UserJSON user = userService.updateDocumentVerificationStatusToDenied(email,deniedReason);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/users/{email}/change-identity-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> temporaryChangeIdentityToDenied(@RequestParam String secret,@PathVariable("email") String email, @RequestBody Map<String,String> body) {
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
            String deniedReason = body.get("deniedReason");
            UserJSON user = userService.updateIdentityVerificationStatusToDenied(email,deniedReason);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/users/{email}/change-document-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> temporaryChangeDocumentToDenied(@RequestParam String secret,@PathVariable("email") String email,@RequestBody Map<String,String> body) {
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
            String deniedReason = body.get("deniedReason");
            UserJSON user = userService.updateDocumentVerificationStatusToDenied(email,deniedReason);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(user);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (UserException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }
}
