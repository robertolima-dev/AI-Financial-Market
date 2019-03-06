package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;

/**
 * Interface responsible for send email WITHDRAWAL statuses changes
 */
public interface WithdrawEmailCourier {

    /**
     * Send Waiting Approval Email
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Processing Email
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailProcessing(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Denied Email
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Cancelled Email
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Confirmed Email
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailConfirmed(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;
}
