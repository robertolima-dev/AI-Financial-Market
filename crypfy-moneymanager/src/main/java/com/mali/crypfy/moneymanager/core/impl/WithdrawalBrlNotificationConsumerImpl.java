package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.WithdrawEmailCourier;
import com.mali.crypfy.moneymanager.core.WithdrawalBrlNotificationConsumer;
import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Implementation of Withdrawal Notifications Consumer
 */
public class WithdrawalBrlNotificationConsumerImpl implements WithdrawalBrlNotificationConsumer{

    final static Logger logger = LoggerFactory.getLogger(WithdrawalBrlNotificationConsumerImpl.class);

    @Autowired
    private WithdrawEmailCourier withdrawEmailCourier;

    @Value("${app.email.no-reply-email}")
    private String noReplyEmail;

    @KafkaListener(topics = "${app.integrations.topics.withdraw-brl.change-status-to-confirmed}", groupId = "withdraw-brl-confirmation-email")
    @Override
    public void onConfirmation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message withdraw brl confirmation, sending confirmation email");
        //send email
        withdrawEmailCourier.sendEmailConfirmed(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.withdraw-brl.change-status-to-denied}", groupId = "withdraw-brl-denied-email")
    @Override
    public void onDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message withdraw brl denied, sending denied email");
        //send email
        withdrawEmailCourier.sendEmailDenied(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.withdraw-brl.change-status-to-processing}", groupId = "withdraw-brl-processing-email")
    @Override
    public void onProcessing(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message withdraw brl processing, sending processing email");
        //send email
        withdrawEmailCourier.sendEmailProcessing(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.withdraw-brl.change-status-to-waiting-approval}", groupId = "withdraw-brl-waiting-approval-email")
    @Override
    public void onWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message withdraw brl waiting approval, sending waiting approval email");
        //send email
        withdrawEmailCourier.sendEmailWaitingApproval(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.withdraw-brl.change-status-to-cancelled}", groupId = "withdraw-brl-cancelled-email")
    @Override
    public void onCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message withdraw brl cancelled, sending cancelled email");
        //send email
        withdrawEmailCourier.sendEmailCancelled(depositWithdrawRequestBrl);
    }
}
