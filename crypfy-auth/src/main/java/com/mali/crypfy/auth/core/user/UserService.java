package com.mali.crypfy.auth.core.user;

import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordInvalidTokenException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordRequestException;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import com.mali.crypfy.auth.persistence.entity.RedefinePasswordRequest;
import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    public User add(User user) throws UserException;
    public User updateProfile(User user) throws UserException;
    public User checkEmailConfirmation(String emailConfirmationToken) throws UserException;
    public User addAvailableBalanceBrl(String email, BigDecimal amount) throws UserException;
    public User getInfo(String email) throws UserException;
    public RedefinePasswordRequest requestNewPassword(String email) throws RedefinePasswordRequestException;
    public boolean isRedefinePasswordTokenValid(String token) throws RedefinePasswordInvalidTokenException;
    public User redefinePassword(String token, String newPassword, String confirmNewPassword) throws RedefinePasswordRequestException;
    public List<User> list(DocumentVerificationStatus documentStatus, IdentityVerificationStatus identityStatus, String email) throws UserException;
    public User redefinePassword(String email,String currentPassword,String newPassword, String confirmNewPassword) throws RedefinePasswordRequestException;
    public User uploadAvatar(String email, String avatar) throws UserException;
    public boolean isAccountVerified(String email) throws UserException;
    public void resendEmailConfirmation(String email) throws UserException;

    public User updateIdentityVerificationStatusToWaitingApproval(String email, String identityVerificationPhoto) throws UserException;
    public User updateDocumentVerificationStatusToWaitingApproval(String email, String documentVerificationPhoto) throws UserException;
    public User updateIdentityVerificationStatusToVerified(String email) throws UserException;
    public User updateDocumentVerificationStatusToVerified(String email) throws UserException;
    public User updateIdentityVerificationStatusToDenied(String email,String deniedReason) throws UserException;
    public User updateDocumentVerificationStatusToDenied(String email,String deniedReason) throws UserException;
}
