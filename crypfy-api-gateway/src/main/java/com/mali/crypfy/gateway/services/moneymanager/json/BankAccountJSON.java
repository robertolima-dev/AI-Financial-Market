package com.mali.crypfy.gateway.services.moneymanager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountJSON {
    private Integer idbankAccount;
    private String email;
    private Integer agency;
    private Integer accountNumber;
    private BankJSON bank;
    private Date created;
    private String type;
    private Integer idbank;
    private Integer accountNumberDigit;
    private String documentType;
    private String documentNumber;

    public Integer getIdbankAccount() {
        return idbankAccount;
    }

    public void setIdbankAccount(Integer idbankAccount) {
        this.idbankAccount = idbankAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAgency() {
        return agency;
    }

    public void setAgency(Integer agency) {
        this.agency = agency;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankJSON getBank() {
        return bank;
    }

    public void setBank(BankJSON bank) {
        this.bank = bank;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIdbank() {
        return idbank;
    }

    public void setIdbank(Integer idbank) {
        this.idbank = idbank;
    }

    public Integer getAccountNumberDigit() {
        return accountNumberDigit;
    }

    public void setAccountNumberDigit(Integer accountNumberDigit) {
        this.accountNumberDigit = accountNumberDigit;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
