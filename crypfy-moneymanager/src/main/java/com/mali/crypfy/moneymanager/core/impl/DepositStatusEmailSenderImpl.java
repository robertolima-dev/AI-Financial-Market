package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.DepositEmailCourier;
import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;
import com.mali.crypfy.moneymanager.core.email.impl.SESMailSender;
import com.mali.crypfy.moneymanager.core.template.HtmlTemplateBuilder;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.BankAccountType;
import com.mali.crypfy.moneymanager.persistence.enumeration.DocumentType;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class DepositStatusEmailSenderImpl implements DepositEmailCourier {

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Autowired
    private HtmlTemplateBuilder htmlTemplateBuilder;
    @Autowired
    private SESMailSender sesMailSender;

    @Value("${app.email.no-reply-email}")
    private String noReplyEmail;

    @Override
    public void sendEmailWaitingPhotoUpload(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {

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
        params.put("bank_razao","Mali Tecnologia Ltda");

        String bodyHtml = htmlTemplateBuilder.buildTemplate("new_deposit_brl.ftl",params);
        sesMailSender.sendSimpleMailHtml(depositWithdrawRequestBrl.getEmail(),noReplyEmail,"Nova intenção de depósito","Crypfy Plataforma",bodyHtml);

    }

    @Override
    public void sendEmailWaitingApproval(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        sendGeneralDepositEmail(depositWithdrawRequestBrl);
    }

    @Override
    public void sendEmailDenied(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {

        Map<String, String> params = new HashMap<String,String>();
        params.put("date",df.format(depositWithdrawRequestBrl.getCreated()));
        params.put("amount",StringUtils.toMoneyFormat(depositWithdrawRequestBrl.getAmount()));
        params.put("denied_reason",depositWithdrawRequestBrl.getDeniedReason());

        String bodyHtml = htmlTemplateBuilder.buildTemplate("denied_deposit_brl.ftl",params);
        sesMailSender.sendSimpleMailHtml(depositWithdrawRequestBrl.getEmail(),noReplyEmail,"Intenção de Depósito Negada","Crypfy Plataforrma",bodyHtml);

    }

    @Override
    public void sendEmailCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        sendGeneralDepositEmail(depositWithdrawRequestBrl);
    }

    @Override
    public void sendEmailConfirmed(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        sendGeneralDepositEmail(depositWithdrawRequestBrl);
    }

    /**
     * Send email to CANCELLED, WAUTING APPROVAL AND CONFIRMED
     * @param depositWithdrawRequestBrl
     * @throws HtmlTemplateBuilderException
     * @throws EmailException
     */
    private void sendGeneralDepositEmail(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws HtmlTemplateBuilderException,EmailException {
        Map<String, String> params = new HashMap<String,String>();
        params.put("date",df.format(depositWithdrawRequestBrl.getCreated()));
        params.put("amount", StringUtils.toMoneyFormat(depositWithdrawRequestBrl.getAmount()));

        String template = null;
        String subject = null;

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.CANCELLED) {
            template = "cancelled_deposit_brl.ftl";
            subject = "Intenção de Depósito Cancelada";
        }

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.WAITING_APPROVAL) {
            template = "waiting_approval_deposit_brl.ftl";
            subject = "Intenção de Depósito Aguardando Aprovação";
        }

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.CONFIRMED) {
            template = "confirmed_deposit_brl.ftl";
            subject = "Intenção de Depósito Confirmada";
        }

        if(template == null)
            return;

        String bodyHtml = htmlTemplateBuilder.buildTemplate(template,params);
        sesMailSender.sendSimpleMailHtml(depositWithdrawRequestBrl.getEmail(),noReplyEmail,subject,"Crypfy Plataforrma",bodyHtml);
    }
}
