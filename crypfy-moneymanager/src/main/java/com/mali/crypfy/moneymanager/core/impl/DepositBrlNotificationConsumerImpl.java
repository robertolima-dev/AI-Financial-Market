package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.DepositBrlNotificationConsumer;
import com.mali.crypfy.moneymanager.core.DepositEmailCourier;
import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Implementation of Deposit Brl Notifications Consumer
 */
@Service
public class DepositBrlNotificationConsumerImpl implements DepositBrlNotificationConsumer {

    final static Logger logger = LoggerFactory.getLogger(DepositBrlNotificationConsumerImpl.class);

    @Autowired
    private DepositEmailCourier depositEmailCourier;

    @Value("${app.email.no-reply-email}")
    private String noReplyEmail;

    @KafkaListener(topics = "${app.integrations.topics.deposit-brl.change-status-to-confirmed}", groupId = "deposit-brl-confirmation-email")
    @Override
    public void onConfirmation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message deposit brl confirmation, sending confirmation email");
        //send email
        depositEmailCourier.sendEmailConfirmed(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.deposit-brl.change-status-to-denied}", groupId = "deposit-brl-denied-email")
    @Override
    public void onDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message deposit brl denied, sending denied email");
        //send email
        depositEmailCourier.sendEmailDenied(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.deposit-brl.change-status-to-cancelled}", groupId = "deposit-brl-cancelled-email")
    @Override
    public void onCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message deposit brl cancelled, sending cancelled email");
        //send email
        depositEmailCourier.sendEmailCancelled(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.deposit-brl.change-status-to-waiting-approval}", groupId = "deposit-brl-waiting-approval-email")
    @Override
    public void onWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message deposit brl waiting approval, sending waiting approval email");
        //send email
        depositEmailCourier.sendEmailWaitingApproval(depositWithdrawRequestBrl);
    }

    @KafkaListener(topics = "${app.integrations.topics.deposit-brl.change-status-to-waiting-photo-upload}", groupId = "deposit-brl-waiting-photo-upload-email")
    @Override
    public void onWaitingPhotoUpload(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws EmailException, HtmlTemplateBuilderException {
        logger.info("consuming message deposit brl waiting photo upload, sending waiting photo upload email");
        //send email
        depositEmailCourier.sendEmailWaitingPhotoUpload(depositWithdrawRequestBrl);
    }
}
