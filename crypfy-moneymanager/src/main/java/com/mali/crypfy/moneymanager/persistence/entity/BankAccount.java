package com.mali.crypfy.moneymanager.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mali.crypfy.moneymanager.persistence.enumeration.BankAccountType;
import com.mali.crypfy.moneymanager.persistence.enumeration.DocumentType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Domain Entity That Represents a Account for any Banks
 */
@Entity
public class BankAccount implements Serializable{

    private Integer idbankAccount;
    private String email;
    private Integer agency;
    private Integer accountNumber;
    private Bank bank;
    private Date created;
    private Integer idbank;
    private BankAccountType type;
    private Integer accountNumberDigit;
    private DocumentType documentType;
    private String documentNumber;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbank_account")
    public Integer getIdbankAccount() {
        return idbankAccount;
    }

    public void setIdbankAccount(Integer idbankAccount) {
        this.idbankAccount = idbankAccount;
    }

    @Column(name = "email",length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "agency")
    public Integer getAgency() {
        return agency;
    }

    public void setAgency(Integer agency) {
        this.agency = agency;
    }

    @Column(name = "account_number")
    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idbank")
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "idbank",insertable = false,updatable = false)
    public Integer getIdbank() {
        return idbank;
    }

    public void setIdbank(Integer idbank) {
        this.idbank = idbank;
    }

    @Column(name = "type",length = 50)
    @Enumerated(EnumType.STRING)
    public BankAccountType getType() {
        return type;
    }

    public void setType(BankAccountType type) {
        this.type = type;
    }

    @Column(name = "account_number_digit")
    public Integer getAccountNumberDigit() {
        return accountNumberDigit;
    }

    public void setAccountNumberDigit(Integer accountNumberDigit) {
        this.accountNumberDigit = accountNumberDigit;
    }

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Column(name = "document_number",length = 60)
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
