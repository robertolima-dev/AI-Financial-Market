package com.mali.crypfy.gateway.services.user;

import com.mali.crypfy.gateway.rest.json.SignupJSON;
import com.mali.crypfy.gateway.services.user.exceptions.EmailConfirmationException;
import com.mali.crypfy.gateway.services.user.exceptions.LoginException;
import com.mali.crypfy.gateway.services.user.exceptions.SignupException;
import com.mali.crypfy.gateway.services.user.exceptions.UserListException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import java.util.List;
import com.mali.crypfy.gateway.services.user.exceptions.UserException;
import com.mali.crypfy.gateway.services.user.json.RedefinePasswordJSON;
import com.mali.crypfy.gateway.services.user.json.UserJSON;

public interface UserService {
    public String login(String email, String password) throws LoginException;
    public String loginAdmin(String email, String password) throws LoginException;
    public void signup(SignupJSON signupJSON) throws SignupException;
    public String emailConfirmation(String tokenEmailConfirmation) throws EmailConfirmationException;
    public void resendEmailConfirmation(String email) throws UserException;
    public boolean isValidToken(String token);

    public List<UserJSON> list(String documentStatus,String identityStatus, String email) throws UserListException;
    public UserJSON redefinePassword(String email,RedefinePasswordJSON redefinePasswordJSON) throws UserException;
    public UserJSON getInfo(String email) throws UserException;
    public UserJSON updateProfile(UserJSON userJSON) throws UserException;
    public UserJSON updateAvatar(String email, String avatar) throws UserException;
    public UserJSON updateIdentityVerificationStatusToWaitingApproval(String email, String identityVerificationPhoto) throws UserException;
    public UserJSON updateDocumentVerificationStatusToWaitingApproval(String email, String documentVerificationPhoto) throws UserException;
    public UserJSON updateIdentityVerificationStatusToVerified(String email) throws UserException;
    public UserJSON updateDocumentVerificationStatusToVerified(String email) throws UserException;
    public UserJSON updateIdentityVerificationStatusToDenied(String email, String deniedReason) throws UserException;
    public UserJSON updateDocumentVerificationStatusToDenied(String email, String deniedReason) throws UserException;

}
