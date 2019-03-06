package com.mali.crypfy.auth.rest;

import com.mali.crypfy.auth.core.user.LoginService;
import com.mali.crypfy.auth.core.user.UserService;
import com.mali.crypfy.auth.core.user.exceptions.LoginException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordInvalidTokenException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordRequestException;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import com.mali.crypfy.auth.persistence.entity.RedefinePasswordRequest;
import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.rest.json.AddAvailableBalanceBrl;
import com.mali.crypfy.auth.rest.json.Login;
import com.mali.crypfy.auth.rest.json.RedefinePassword;
import com.mali.crypfy.auth.rest.json.RequestNewPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class UserRestService {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "user" ,method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody User user) {
        RestResponse restResponse = new RestResponse();
        try {
            user = userService.add(user);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setMessage("Usuário criado com sucesso");
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.CREATED);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Login login) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = loginService.login(login.getEmail(),login.getPassword());
            restResponse.setSuccess(true);
            restResponse.setStatus(201);
            restResponse.setMessage("Login Efetuado com sucesso");
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.CREATED);
        } catch (LoginException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

}
