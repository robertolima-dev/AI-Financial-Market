package com.mali.crypfy.auth.persistence.entity;

import com.mali.crypfy.auth.persistence.enumeration.DocumentType;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class User implements Serializable {

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
    private BigDecimal availableBalanceBrl;
    private DocumentVerificationStatus documentVerificationStatus;
    private IdentityVerificationStatus identityVerificationStatus;
    private String identityVerificationPhoto;
    private String documentVerificationPhoto;
    private String phone;
    private String avatar;
    private String deniedReasonDocumentVerification;
    private String deniedReasonIdentityVerification;

    @Id
    @Column(name = "email",length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "name",length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "password",length = 200)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "document_number",length = 60)
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        if(documentNumber != null)
            this.documentNumber = documentNumber.replace(".","");
        else
            this.documentNumber = documentNumber;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type",length = 10)
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Column(name = "email_confirmation_token",length = 200)
    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(String emailConfirmationToken) {
        this.emailConfirmationToken = emailConfirmationToken;
    }

    @Column(name = "email_verified")
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Column(name = "available_balance_brl",precision = 10, scale = 2)
    public BigDecimal getAvailableBalanceBrl() {
        return availableBalanceBrl;
    }

    public void setAvailableBalanceBrl(BigDecimal availableBalanceBrl) {
        this.availableBalanceBrl = availableBalanceBrl;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "document_verification_status",length = 50)
    public DocumentVerificationStatus getDocumentVerificationStatus() {
        return documentVerificationStatus;
    }

    public void setDocumentVerificationStatus(DocumentVerificationStatus documentVerificationStatus) {
        this.documentVerificationStatus = documentVerificationStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_verification_status",length = 50)
    public IdentityVerificationStatus getIdentityVerificationStatus() {
        return identityVerificationStatus;
    }

    public void setIdentityVerificationStatus(IdentityVerificationStatus identityVerificationStatus) {
        this.identityVerificationStatus = identityVerificationStatus;
    }

    @Column(name = "identity_verification_photo",length = 500)
    public String getIdentityVerificationPhoto() {
        return identityVerificationPhoto;
    }

    public void setIdentityVerificationPhoto(String identityVerificationPhoto) {
        this.identityVerificationPhoto = identityVerificationPhoto;
    }

    @Column(name = "document_verification_photo",length = 500)
    public String getDocumentVerificationPhoto() {
        return documentVerificationPhoto;
    }

    public void setDocumentVerificationPhoto(String documentVerificationPhoto) {
        this.documentVerificationPhoto = documentVerificationPhoto;
    }

    @Column(name = "phone",length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "avatar",length = 800)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(name = "denied_reason_document_verification",length = 3000)
    public String getDeniedReasonDocumentVerification() {
        return deniedReasonDocumentVerification;
    }

    public void setDeniedReasonDocumentVerification(String deniedReasonDocumentVerification) {
        this.deniedReasonDocumentVerification = deniedReasonDocumentVerification;
    }

    @Column(name = "denied_reason_identity_verification",length = 3000)
    public String getDeniedReasonIdentityVerification() {
        return deniedReasonIdentityVerification;
    }

    public void setDeniedReasonIdentityVerification(String deniedReasonIdentityVerification) {
        this.deniedReasonIdentityVerification = deniedReasonIdentityVerification;
    }
}
