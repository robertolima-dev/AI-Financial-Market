package com.mali.crypfy.moneymanager.integration.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mali.crypfy.moneymanager.persistence.enumeration.DocumentType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User Entity Pojo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String email;
    private String name;
    private String password;
    private String confirmPassword;
    private String documentNumber;
    private DocumentType documentType;
    private Date created;
    private Date lastLogin;
    private String emailConfirmationToken;
    private boolean emailVerified;
    private Double availableBalanceBrl;
    private String documentVerificationStatus;
    private String identityVerificationStatus;
    private String identityVerificationPhoto;
    private String documentVerificationPhoto;
    private String phone;
    private String avatar;
    private String deniedReasonDocumentVerification;
    private String deniedReasonIdentityVerification;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAvailableBalanceBrl() {
        return availableBalanceBrl;
    }

    public void setAvailableBalanceBrl(Double availableBalanceBrl) {
        this.availableBalanceBrl = availableBalanceBrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(String emailConfirmationToken) {
        this.emailConfirmationToken = emailConfirmationToken;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getDocumentVerificationStatus() {
        return documentVerificationStatus;
    }

    public void setDocumentVerificationStatus(String documentVerificationStatus) {
        this.documentVerificationStatus = documentVerificationStatus;
    }

    public String getIdentityVerificationStatus() {
        return identityVerificationStatus;
    }

    public void setIdentityVerificationStatus(String identityVerificationStatus) {
        this.identityVerificationStatus = identityVerificationStatus;
    }

    public String getIdentityVerificationPhoto() {
        return identityVerificationPhoto;
    }

    public void setIdentityVerificationPhoto(String identityVerificationPhoto) {
        this.identityVerificationPhoto = identityVerificationPhoto;
    }

    public String getDocumentVerificationPhoto() {
        return documentVerificationPhoto;
    }

    public void setDocumentVerificationPhoto(String documentVerificationPhoto) {
        this.documentVerificationPhoto = documentVerificationPhoto;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDeniedReasonDocumentVerification() {
        return deniedReasonDocumentVerification;
    }

    public void setDeniedReasonDocumentVerification(String deniedReasonDocumentVerification) {
        this.deniedReasonDocumentVerification = deniedReasonDocumentVerification;
    }

    public String getDeniedReasonIdentityVerification() {
        return deniedReasonIdentityVerification;
    }

    public void setDeniedReasonIdentityVerification(String deniedReasonIdentityVerification) {
        this.deniedReasonIdentityVerification = deniedReasonIdentityVerification;
    }
}
