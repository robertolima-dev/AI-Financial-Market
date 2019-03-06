package com.mali.crypfy.gateway.services.moneymanager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mali.crypfy.gateway.services.moneymanager.enumeration.Status;
import com.mali.crypfy.gateway.services.moneymanager.enumeration.TypeDepositWithdraw;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositWithdrawRequestlBrlJSON {
    private Integer iddepositWithdrawRequestBrl;
    private BankAccountJSON bankAccount;
    private TypeDepositWithdraw type;
    private Date created;
    private Status status;
    private BigDecimal amount;
    private String photo;
    private Date lastUpdated;
    private String email;
    private Integer idbankAccount;
    private String bankName;
    private Integer bankCode;
    private String bankLogo;
    private Integer bankAgency;
    private Integer bankAccountNumber;
    private String bankAccountType;
    private Integer bankAccountNumberDigit;
    private String bankAccountDocumentType;
    private String bankAccountDocumentNumber;
    private BigDecimal fee;
    private String deniedReason;

    public Integer getIddepositWithdrawRequestBrl() {
        return iddepositWithdrawRequestBrl;
    }

    public void setIddepositWithdrawRequestBrl(Integer iddepositWithdrawRequestBrl) {
        this.iddepositWithdrawRequestBrl = iddepositWithdrawRequestBrl;
    }

    public BankAccountJSON getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountJSON bankAccount) {
        this.bankAccount = bankAccount;
    }

    public TypeDepositWithdraw getType() {
        return type;
    }

    public void setType(TypeDepositWithdraw type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdbankAccount() {
        return idbankAccount;
    }

    public void setIdbankAccount(Integer idbankAccount) {
        this.idbankAccount = idbankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBankCode() {
        return bankCode;
    }

    public void setBankCode(Integer bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public Integer getBankAgency() {
        return bankAgency;
    }

    public void setBankAgency(Integer bankAgency) {
        this.bankAgency = bankAgency;
    }

    public Integer getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(Integer bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public Integer getBankAccountNumberDigit() {
        return bankAccountNumberDigit;
    }

    public void setBankAccountNumberDigit(Integer bankAccountNumberDigit) {
        this.bankAccountNumberDigit = bankAccountNumberDigit;
    }

    public String getBankAccountDocumentType() {
        return bankAccountDocumentType;
    }

    public void setBankAccountDocumentType(String bankAccountDocumentType) {
        this.bankAccountDocumentType = bankAccountDocumentType;
    }

    public String getBankAccountDocumentNumber() {
        return bankAccountDocumentNumber;
    }

    public void setBankAccountDocumentNumber(String bankAccountDocumentNumber) {
        this.bankAccountDocumentNumber = bankAccountDocumentNumber;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getDeniedReason() {
        return deniedReason;
    }

    public void setDeniedReason(String deniedReason) {
        this.deniedReason = deniedReason;
    }
}
