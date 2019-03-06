package com.mali.crypfy.moneymanager.persistence.entity;

import com.mali.crypfy.moneymanager.persistence.enumeration.BankAccountType;
import com.mali.crypfy.moneymanager.persistence.enumeration.DocumentType;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.TypeDepositWithdraw;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Domain Entity That Represents a Brl Deposit Request OR Brl Withdrawal Request
 */
@Entity
public class DepositWithdrawRequestBrl implements Serializable {

    private Integer iddepositWithdrawRequestBrl;
    private TypeDepositWithdraw type;
    private Date created;
    private StatusDepositWithdrawBrl status;
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
    private Integer bankAccountNumberDigit;
    private BankAccountType bankAccountType;
    private Integer iduserPlan;
    private DocumentType bankAccountDocumentType;
    private String bankAccountDocumentNumber;
    private BigDecimal fee;
    private String deniedReason;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddeposit_withdraw_request_brl")
    public Integer getIddepositWithdrawRequestBrl() {
        return iddepositWithdrawRequestBrl;
    }

    public void setIddepositWithdrawRequestBrl(Integer iddepositWithdrawRequestBrl) {
        this.iddepositWithdrawRequestBrl = iddepositWithdrawRequestBrl;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type",length = 15)
    public TypeDepositWithdraw getType() {
        return type;
    }

    public void setType(TypeDepositWithdraw type) {
        this.type = type;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public StatusDepositWithdrawBrl getStatus() {
        return status;
    }

    public void setStatus(StatusDepositWithdrawBrl status) {
        this.status = status;
    }

    @Column(name = "amount",precision = 10, scale = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "photo",length = 800)
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Column(name = "email",length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    public Integer getIdbankAccount() {
        return idbankAccount;
    }

    public void setIdbankAccount(Integer idbankAccount) {
        this.idbankAccount = idbankAccount;
    }

    @Column(name = "bank_name",length = 100)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "bank_code")
    public Integer getBankCode() {
        return bankCode;
    }

    public void setBankCode(Integer bankCode) {
        this.bankCode = bankCode;
    }

    @Column(name = "bank_logo",length = 100)
    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    @Column(name = "bank_agency")
    public Integer getBankAgency() {
        return bankAgency;
    }

    public void setBankAgency(Integer bankAgency) {
        this.bankAgency = bankAgency;
    }

    @Column(name = "bank_account_number")
    public Integer getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(Integer bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_type")
    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    @Column(name = "bank_account_number_digit")
    public Integer getBankAccountNumberDigit() {
        return bankAccountNumberDigit;
    }

    public void setBankAccountNumberDigit(Integer bankAccountNumberDigit) {
        this.bankAccountNumberDigit = bankAccountNumberDigit;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_document_type",length = 10)
    public DocumentType getBankAccountDocumentType() {
        return bankAccountDocumentType;
    }

    public void setBankAccountDocumentType(DocumentType bankAccountDocumentType) {
        this.bankAccountDocumentType = bankAccountDocumentType;
    }

    @Column(name = "bank_account_document_number",length = 60)
    public String getBankAccountDocumentNumber() {
        return bankAccountDocumentNumber;
    }

    public void setBankAccountDocumentNumber(String bankAccountDocumentNumber) {
        this.bankAccountDocumentNumber = bankAccountDocumentNumber;
    }

    @Column(name = "fee",precision = 10, scale = 2)
    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Column(name = "denied_reason",length = 5000)
    public String getDeniedReason() {
        return deniedReason;
    }

    public void setDeniedReason(String deniedReason) {
        this.deniedReason = deniedReason;
    }
}
