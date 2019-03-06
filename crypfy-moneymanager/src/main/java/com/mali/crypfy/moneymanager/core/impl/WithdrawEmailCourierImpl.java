package com.mali.crypfy.moneymanager.core.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.moneymanager.core.WithdrawEmailCourier;
import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.email.impl.SESMailSender;
import com.mali.crypfy.moneymanager.core.template.HtmlTemplateBuilder;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.BankAccountType;
import com.mali.crypfy.moneymanager.persistence.enumeration.DocumentType;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class WithdrawEmailCourierImpl implements WithdrawEmailCourier {

    final static Logger logger = LoggerFactory.getLogger(WithdrawEmailCourierImpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Autowired
    private HtmlTemplateBuilder htmlTemplateBuilder;
    @Autowired
    private SESMailSender sesMailSender;

    @Value("${app.email.no-reply-email}")
    private String noReplyEmail;

    @Override
    public void sendEmailWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {

        Map<String, String> params = new HashMap<String,String>();
        params.put("date",df.format(depositWithdrawRequestBrl.getCreated()));
        params.put("amount",StringUtils.toMoneyFormat(depositWithdrawRequestBrl.getAmount()));
        params.put("bank_name",depositWithdrawRequestBrl.getBankName());
        params.put("bank_agency",depositWithdrawRequestBrl.getBankAgency().toString());
        params.put("bank_account_number",depositWithdrawRequestBrl.getBankAccountNumber().toString());
        params.put("bank_account_number_digit",depositWithdrawRequestBrl.getBankAccountNumberDigit().toString());
        params.put("bank_account_type", BankAccountType.getHumanName(depositWithdrawRequestBrl.getBankAccountType()));
        params.put("bank_document_type",depositWithdrawRequestBrl.getBankAccountDocumentType().toString());
        if(depositWithdrawRequestBrl.getBankAccountDocumentType() == DocumentType.CNPJ)
            params.put("bank_document_number",StringUtils.formatCnpj(depositWithdrawRequestBrl.getBankAccountDocumentNumber()));
        else
            params.put("bank_document_number",StringUtils.formatCpf(depositWithdrawRequestBrl.getBankAccountDocumentNumber()));

        String bodyHtml = htmlTemplateBuilder.buildTemplate("new_withdraw_brl.ftl",params);
        sesMailSender.sendSimpleMailHtml(depositWithdrawRequestBrl.getEmail(),noReplyEmail,"Nova intenção de Saque","Crypfy Plataforma",bodyHtml);

    }

    @Override
    public void sendEmailProcessing(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        sendGeneralWithdrawEmail(depositWithdrawRequestBrl);
    }

    @Override
    public void sendEmailDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        try {
            Map<String, String> params = new HashMap<String,String>();
            params.put("date",df.format(depositWithdrawRequestBrl.getCreated()));
            params.put("amount",StringUtils.toMoneyFormat(depositWithdrawRequestBrl.getAmount()));
            params.put("denied_reason",depositWithdrawRequestBrl.getDeniedReason());

            String bodyHtml = htmlTemplateBuilder.buildTemplate("denied_withdraw_brl.ftl",params);
            sesMailSender.sendSimpleMailHtml(depositWithdrawRequestBrl.getEmail(),noReplyEmail,"Intenção de Saque Negada","Crypfy Plataforrma",bodyHtml);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on generating html for new deposit brlr email",e);
        } catch (EmailException e) {
            logger.error("error on sending email for new deposit brlr email",e);
        }
    }

    @Override
    public void sendEmailCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        sendGeneralWithdrawEmail(depositWithdrawRequestBrl);
    }

    @Override
    public void sendEmailConfirmed(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        sendGeneralWithdrawEmail(depositWithdrawRequestBrl);
    }

    /**
     * Send Email Notification to Statuses Changes CANCELLED, PROCESSING AND CONFIRMED
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    private void sendGeneralWithdrawEmail(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        Map<String, String> params = new HashMap<String,String>();
        params.put("date",df.format(depositWithdrawRequestBrl.getCreated()));
        params.put("amount", StringUtils.toMoneyFormat(depositWithdrawRequestBrl.getAmount()));

        String template = null;
        String subject = null;

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.CANCELLED) {
            template = "cancelled_withdraw_brl.ftl";
            subject = "Intenção de Saque Cancelada";
        }

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.PROCESSING) {
            template = "processing_withdraw_brl.ftl";
            subject = "Intenção de Saque em Processamento";
        }

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.CONFIRMED) {
            template = "confirmed_withdraw_brl.ftl";
            subject = "Intenção de Saque Confirmada";
        }

        if(template == null)
            return;

        String bodyHtml = htmlTemplateBuilder.buildTemplate(template,params);
        sesMailSender.sendSimpleMailHtml(depositWithdrawRequestBrl.getEmail(),noReplyEmail,subject,"Crypfy Plataforrma",bodyHtml);
    }
}
