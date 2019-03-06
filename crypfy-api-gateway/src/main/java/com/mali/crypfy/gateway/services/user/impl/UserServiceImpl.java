package com.mali.crypfy.gateway.services.user.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.rest.json.LoginJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseListJSON;
import com.mali.crypfy.gateway.rest.json.SignupJSON;
import com.mali.crypfy.gateway.services.ServiceItemError;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.UserService;
import com.mali.crypfy.gateway.services.user.exceptions.*;
import com.mali.crypfy.gateway.services.user.json.RedefinePasswordJSON;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public static final int CODE_ERROR_GENERIC = 9000;

    @Value("${spring.services.user.login}")
    private String loginServiceResource;
    @Value("${spring.services.user.signup}")
    private String signupServiceResource;
    @Value("${spring.services.user.email-confirmation}")
    private String emailConfirmationServiceResource;
    @Value("${spring.services.admin-user.login}")
    private String loginAdminServiceResource;
    @Value("${spring.services.user.list}")
    private String listUserResource;
    @Value("${spring.services.user.redefine-password}")
    private String redefinePasswordResource;
    @Value("${spring.services.user.get-info}")
    private String getInfoResource;
    @Value("${spring.services.user.update-profile}")
    private String updateProfileResource;
    @Value("${spring.services.user.update-avatar}")
    private String updateAvatarResource;
    @Value("${spring.services.user.change-identity-verification-status-to-waiting-approval}")
    private String changeIdentityVerificationStatusToWaitingApprovalResource;
    @Value("${spring.services.user.change-document-verification-status-to-waiting-approval}")
    private String changeDocumentVerificationStatusToWaitingApprovalResource;
    @Value("${spring.services.user.change-identity-verification-status-to-verified}")
    private String changeIdentityVerificationStatusToVerifiedResource;
    @Value("${spring.services.user.change-document-verification-status-to-verified}")
    private String changeDocumentVerificationStatusToVerifiedResource;
    @Value("${spring.services.user.change-identity-verification-status-to-denied}")
    private String changeIdentityVerificationStatusToDeniedResource;
    @Value("${spring.services.user.change-document-verification-status-to-denied}")
    private String changeDocumentVerificationStatusToDeniedResource;

    @Value("${spring.services.user.resend-email-confirmation}")
    private String resendEmailConfirmationResource;

    @Autowired
    @Qualifier("admin")
    private JWTAuthBuilder jwtAdminAuthBuilder;
    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String login(String email, String password) throws LoginException {

        RestTemplate restTemplate = new RestTemplate();

        LoginJSON login = new LoginJSON();
        login.setEmail(email);
        login.setPassword(password);

        HttpEntity<LoginJSON> request = new HttpEntity<>(login);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(loginServiceResource, HttpMethod.POST, request, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new LoginException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new LoginException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else {
            //build jwt token
            try {
                UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
                return jwtAuthBuilder.sign(userJSON);
            } catch (JWTAuthBuilderException e) {
                logger.error("error on build jwt token",e);
                throw new LoginException(e.getMessage(),null,500);
            }
        }
    }

    @Override
    public String loginAdmin(String email, String password) throws LoginException {
        RestTemplate restTemplate = new RestTemplate();

        LoginJSON login = new LoginJSON();
        login.setEmail(email);
        login.setPassword(password);

        HttpEntity<LoginJSON> request = new HttpEntity<>(login);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(loginAdminServiceResource, HttpMethod.POST, request, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new LoginException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        //if there is an error
        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new LoginException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        } else {
            //build jwt token
            try {
                UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
                return jwtAdminAuthBuilder.sign(userJSON);
            } catch (JWTAuthBuilderException e) {
                logger.error("error on build jwt token",e);
                throw new LoginException(e.getMessage(),null,500);
            }
        }
    }

    @Override
    public void signup(SignupJSON signupJSON) throws SignupException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<SignupJSON> request = new HttpEntity<>(signupJSON);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(signupServiceResource, HttpMethod.POST, request, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new SignupException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new SignupException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public String emailConfirmation(String tokenEmailConfirmation) throws EmailConfirmationException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(emailConfirmationServiceResource+"?token="+tokenEmailConfirmation, HttpMethod.PUT,null, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new EmailConfirmationException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(restResponseJSON.isSuccess()) {
            try {
                UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
                return jwtAuthBuilder.sign(userJSON);
            } catch (JWTAuthBuilderException e) {
                logger.error("error on build jwt token",e);
                throw new EmailConfirmationException("Ocorreu um erro no servidor",null,500);
            }
        }
        else
            throw new EmailConfirmationException(restResponseJSON.getMessage(),null,restResponseJSON.getStatus());
    }

    @Override
    public void resendEmailConfirmation(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        String resource = resendEmailConfirmationResource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource, HttpMethod.POST, null, RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if (!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(), serviceItemErrors,restResponseJSON.getStatus());
        }
    }

    @Override
    public boolean isValidToken(String token) {
        return jwtAuthBuilder.isTokenValid(token);
    }

    @Override
    public List<UserJSON> list(String documentStatus,String identityStatus, String email) throws UserListException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(listUserResource);

        if(email != null && !email.equals(""))
            builder.queryParam("email",email);
        if(documentStatus != null && !documentStatus.equals(""))
            builder.queryParam("documentStatus",documentStatus);
        if(identityStatus != null && !identityStatus.equals(""))
            builder.queryParam("identityStatus",identityStatus);

        String uri = builder.build().encode().toUri().toString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponseListJSON<UserJSON>> response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<RestResponseListJSON<UserJSON>>() {
        });

        //if status not 200/201/20
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)
            throw new UserListException("Ocorreu um erro no servidor", 500);

        RestResponseListJSON<UserJSON> restResponseJSON = response.getBody();
        if (restResponseJSON.isSuccess())
            return (List<UserJSON>) restResponseJSON.getResponse();
        else
            throw new UserListException(restResponseJSON.getMessage(), restResponseJSON.getStatus());
    }

    public UserJSON redefinePassword(String email,RedefinePasswordJSON redefinePasswordJSON) throws UserException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<RedefinePasswordJSON> entity = new HttpEntity<RedefinePasswordJSON>(redefinePasswordJSON);

        String resource = redefinePasswordResource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,entity,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON getInfo(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();
        String resource = getInfoResource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.GET,null,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateProfile(UserJSON userJSON) throws UserException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserJSON> entity = new HttpEntity<UserJSON>(userJSON);
        String resource = updateProfileResource.replace("{email}",userJSON.getEmail());

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,entity,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateAvatar(String email, String avatar) throws UserException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(avatar);
        String resource = updateAvatarResource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,entity,RestResponseJSON.class);

        //if status not 200/201/202
        if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            //create list error
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors,500);
        }

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateIdentityVerificationStatusToWaitingApproval(String email, String identityVerificationPhoto) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeIdentityVerificationStatusToWaitingApprovalResource.replace("{email}",email);

        HttpEntity<String> request = new HttpEntity<String>(identityVerificationPhoto,headers);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateDocumentVerificationStatusToWaitingApproval(String email, String documentVerificationPhoto) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeDocumentVerificationStatusToWaitingApprovalResource.replace("{email}",email);

        HttpEntity<String> request = new HttpEntity<String>(documentVerificationPhoto,headers);
        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateIdentityVerificationStatusToVerified(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeIdentityVerificationStatusToVerifiedResource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateDocumentVerificationStatusToVerified(String email) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeDocumentVerificationStatusToVerifiedResource.replace("{email}",email);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,null,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateIdentityVerificationStatusToDenied(String email, String deniedReason) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeIdentityVerificationStatusToDeniedResource.replace("{email}",email);

        Map<String,String> body = new HashMap<String,String>();
        body.put("deniedReason",deniedReason);
        HttpEntity<Map<String,String>> request = new HttpEntity<>(body);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }

    @Override
    public UserJSON updateDocumentVerificationStatusToDenied(String email, String deniedReason) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = changeDocumentVerificationStatusToDeniedResource.replace("{email}",email);

        Map<String,String> body = new HashMap<String,String>();
        body.put("deniedReason",deniedReason);
        HttpEntity<Map<String,String>> request = new HttpEntity<>(body);

        ResponseEntity<RestResponseJSON> response = restTemplate.exchange(resource,HttpMethod.PUT,request,RestResponseJSON.class);

        RestResponseJSON restResponseJSON = response.getBody();

        if(!restResponseJSON.isSuccess()) {
            List<ServiceItemError> serviceItemErrors = (List<ServiceItemError>) restResponseJSON.getResponse();
            throw new UserException(restResponseJSON.getMessage(),serviceItemErrors,restResponseJSON.getStatus());
        } else {
            UserJSON userJSON = mapper.convertValue(restResponseJSON.getResponse(),UserJSON.class);
            return userJSON;
        }
    }


}
