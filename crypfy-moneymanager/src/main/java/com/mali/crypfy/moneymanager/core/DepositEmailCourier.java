package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;

/**
 * Interface responsible for send email DEPOSIT statuses changes
 */
public interface DepositEmailCourier {

    /**
     * Send Email Waiting Photo Upload Notification
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailWaitingPhotoUpload(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Email Waiting Approval Notification
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Email Denied Notification
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Email Cancelled Notification
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

    /**
     * Send Email Confirmed Notification
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    public void sendEmailConfirmed(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException;

}
