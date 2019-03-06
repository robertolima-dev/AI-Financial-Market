package com.mali.crypfy.auth.rest;

import com.mali.crypfy.auth.core.user.LoginService;
import com.mali.crypfy.auth.core.user.UserService;
import com.mali.crypfy.auth.core.user.exceptions.LoginException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordInvalidTokenException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordRequestException;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import com.mali.crypfy.auth.persistence.entity.RedefinePasswordRequest;
import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;
import com.mali.crypfy.auth.rest.json.AddAvailableBalanceBrl;
import com.mali.crypfy.auth.rest.json.Login;
import com.mali.crypfy.auth.rest.json.RedefinePassword;
import com.mali.crypfy.auth.rest.json.RequestNewPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestService {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "users" ,method = RequestMethod.POST)
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

    @RequestMapping(value = "/users/{email:.+}/add-available-balance-brl" ,method = RequestMethod.PUT)
    public ResponseEntity<?> addAvailableBalanceBrl(@PathVariable String email, @RequestBody Map<String,Double> params) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.addAvailableBalanceBrl(email, new BigDecimal(params.get("amount").toString()));
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("Saldo atualizado com sucesso");
            restResponse.setResponse(user.getAvailableBalanceBrl());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/update-avatar" ,method = RequestMethod.PUT)
    public ResponseEntity<?> updateAvatar(@PathVariable String email,@RequestBody String avatar) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.uploadAvatar(email,avatar);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("Avatar atualizado com sucesso");
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/users/{email:.+}/redefine-password" ,method = RequestMethod.PUT)
    public ResponseEntity<?> redefinePassword(@PathVariable String email, @RequestBody Map<String,String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.redefinePassword(email,params.get("currentPassword"),params.get("newPassword"),params.get("confirmNewPassword"));
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setMessage("Senha atualizada com sucesso!");
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (RedefinePasswordRequestException e) {
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

    @RequestMapping(value = "/users/{email:.+}/resend-email-confirmation" ,method = RequestMethod.POST)
    public ResponseEntity<?> resendEmailConfirmation(@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            userService.resendEmailConfirmation(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(201);
            restResponse.setMessage("Email reenviado com sucesso!");
            restResponse.setResponse(null);
            return new ResponseEntity<>(restResponse,HttpStatus.CREATED);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users/{email:.+}" ,method = RequestMethod.GET)
    public ResponseEntity<?> getInfo(@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.getInfo(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users/{email:.+}" ,method = RequestMethod.PUT)
    public ResponseEntity<?> updateProfile(@PathVariable String email,@RequestBody User user) {
        RestResponse restResponse = new RestResponse();
        try {
            user.setEmail(email);
            user = userService.updateProfile(user);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            restResponse.setMessage("Perfil atualizado com sucesso!");
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users/{email:.+}/request-new-password", method = RequestMethod.POST)
    public ResponseEntity<?> requestNewPassword(@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            RedefinePasswordRequest redefinePasswordRequest = userService.requestNewPassword(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(201);
            restResponse.setResponse(redefinePasswordRequest);
            return new ResponseEntity<>(redefinePasswordRequest,HttpStatus.CREATED);
        } catch (RedefinePasswordRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setResponse(e.getErrors());
            restResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "is-request-new-password-token-valid" ,method = RequestMethod.GET)
    public ResponseEntity<?> isTokenRedefinePasswordValid(@RequestParam String token) {
        RestResponse restResponse = new RestResponse();
        try {
            boolean isValidToken = userService.isRedefinePasswordTokenValid(token);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(isValidToken);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (RedefinePasswordInvalidTokenException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "redefine-password" ,method = RequestMethod.PUT )
    public ResponseEntity<?> redefinePassword(@RequestParam String token, @RequestBody RedefinePassword redefinePassword) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.redefinePassword(token,redefinePassword.getNewPassword(),redefinePassword.getConfirmNewPassword());
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (RedefinePasswordRequestException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "email-confirmation" ,method = RequestMethod.PUT)
    public ResponseEntity<?> checkEmailConfirmation(@RequestParam("token") String emailConfirmationToken) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.checkEmailConfirmation(emailConfirmationToken);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users/{email:.+}/is-account-verified" ,method = RequestMethod.GET)
    public ResponseEntity<?> isAccountVerified(@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            boolean isAccountVerified = userService.isAccountVerified(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(isAccountVerified);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users" ,method = RequestMethod.GET)
    public ResponseEntity<?> list(@RequestParam(required = false) DocumentVerificationStatus documentStatus, @RequestParam(required = false) IdentityVerificationStatus identityStatus, @RequestParam(required = false) String email) {
        RestResponse restResponse = new RestResponse();
        try {
            List<User> users = userService.list(documentStatus,identityStatus,email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(users);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/change-identity-verification-status-to-waiting-approval" ,method = RequestMethod.PUT)
    public ResponseEntity<?> changeIdentityVerificationStatusToWaitingApproval(@PathVariable String email,@RequestBody String photo) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.updateIdentityVerificationStatusToWaitingApproval(email,photo);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/change-document-verification-status-to-waiting-approval" ,method = RequestMethod.PUT)
    public ResponseEntity<?> changeDocumentVerificationStatusToWaitingApproval(@PathVariable String email,@RequestBody String photo) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.updateDocumentVerificationStatusToWaitingApproval(email,photo);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/change-identity-verification-status-to-verified" ,method = RequestMethod.PUT)
    public ResponseEntity<?> changeIdentityVerificationStatusToVerified(@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.updateIdentityVerificationStatusToVerified(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/change-document-verification-status-to-verified" ,method = RequestMethod.PUT)
    public ResponseEntity<?> changeDocumentVerificationStatusToVerified(@PathVariable String email) {
        RestResponse restResponse = new RestResponse();
        try {
            User user = userService.updateDocumentVerificationStatusToVerified(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/change-identity-verification-status-to-denied" ,method = RequestMethod.PUT)
    public ResponseEntity<?> changeIdentityVerificationStatusToDenied(@PathVariable String email,@RequestBody Map<String,String> body) {
        RestResponse restResponse = new RestResponse();
        try {
            String deniedReason = body.get("deniedReason");
            User user = userService.updateIdentityVerificationStatusToDenied(email,deniedReason);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{email:.+}/change-document-verification-status-to-denied" ,method = RequestMethod.PUT)
    public ResponseEntity<?> changeDocumentVerificationStatusToDenied(@PathVariable String email,@RequestBody Map<String,String> body) {
        RestResponse restResponse = new RestResponse();
        try {
            String deniedReason = body.get("deniedReason");
            User user = userService.updateDocumentVerificationStatusToDenied(email,deniedReason);
            restResponse.setSuccess(true);
            restResponse.setStatus(200);
            restResponse.setResponse(user);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }



}
