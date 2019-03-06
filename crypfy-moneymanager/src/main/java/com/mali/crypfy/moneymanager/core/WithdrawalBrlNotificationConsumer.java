package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;

/**
 * Interface Witch Consumes a Brl Withdrawals Events from a Topic
 */
public interface WithdrawalBrlNotificationConsumer {

    /**
     * Consumes a topic and send confirmation email
     * @param depositWithdrawRequestBrl
     * @throws EmailException
     * @throws HtmlTemplateBuilderException
     */
    public void onConfirmation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException;

    /**
     * Consumes a topic and send denied email
     * @param depositWithdrawRequestBrl
     * @throws EmailException
     * @throws HtmlTemplateBuilderException
     */
    public void onDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException;

    /**
     * Consumes a topic and send processing email
     * @param depositWithdrawRequestBrl
     * @throws EmailException
     * @throws HtmlTemplateBuilderException
     */
    public void onProcessing(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException;

    /**
     * Consumes a topic and send waiting approval email
     * @param depositWithdrawRequestBrl
     * @throws EmailException
     * @throws HtmlTemplateBuilderException
     */
    public void onWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException;

    /**
     * Consumes a topic and send cancelled email
     * @param depositWithdrawRequestBrl
     * @throws EmailException
     * @throws HtmlTemplateBuilderException
     */
    public void onCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException;

}
