package com.mali.crypfy.indexmanager.integrations.email.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.mali.crypfy.indexmanager.integrations.email.MailSender;
import com.mali.crypfy.indexmanager.integrations.email.exception.EmailException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SESMailSender implements MailSender {

    final static Logger logger = LoggerFactory.getLogger(SESMailSender.class);
    private AmazonSimpleEmailService simpleEmailServiceClient;

    @Value("${spring.aws.ses.access-key}")
    private String accessKey;
    @Value("${spring.aws.ses.secret-key}")
    private String secretKey;

    @PostConstruct
    private void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        simpleEmailServiceClient = AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();
    }

    @Async
    @Override
    public void sendSimpleMailText(String destination, String from, String subject, String title,String bodyText) throws EmailException {
        try {
            Destination dest = new Destination().withBccAddresses(new String[]{destination});
            Content subjectText = new Content().withData(subject);

            //from header
            byte[] b = Base64.encodeBase64(title.getBytes());
            String fromHeader = "=?utf-8?b?"+new String(b)+"?= <"+from+">";

            Content body = new Content().withData(bodyText);
            Body bodyObj = new Body().withText(body);

            // create a message
            Message message = new Message().withSubject(subjectText).withBody(bodyObj);
            SendEmailRequest request = new SendEmailRequest().withSource(fromHeader).withDestination(dest).withMessage(message);

            //send the email
            simpleEmailServiceClient.sendEmail(request);
        } catch (Exception e) {
            logger.error("error on send email",e);
            throw new EmailException("ocorreu um erro ao enviar o email");
        }
    }

    @Async
    @Override
    public void sendSimpleMailHtml(String destination, String from, String subject, String title, String bodyHtml) throws EmailException {
        try {
            //dest
            Destination dest = new Destination().withBccAddresses(new String[]{destination});

            //subject
            Content subjectText = new Content().withData(subject);

            //from header
            byte[] b = Base64.encodeBase64(title.getBytes());
            String fromHeader = "=?utf-8?b?"+new String(b)+"?= <"+from+">";

            Content bodyText = new Content().withData(bodyHtml);
            Body bodyObj = new Body().withHtml(bodyText);

            // create a message
            Message message = new Message().withSubject(subjectText).withBody(bodyObj);
            SendEmailRequest request = new SendEmailRequest().withSource(fromHeader).withDestination(dest).withMessage(message);

            //send the email
            simpleEmailServiceClient.sendEmail(request);
        } catch (Exception e) {
            logger.error("error on send email",e);
            throw new EmailException("ocorreu um erro ao enviar o email");
        }
    }

    @Async
    @Override
    public void sendSimpleMailHtml(String destination, String from, String subject, String title, List<String> cc, String bodyHtml) throws EmailException {
        try {
            Destination dest = new Destination().withBccAddresses(new String[]{destination});
            dest.setCcAddresses(cc);

            Content subjectText = new Content().withData(subject);

            //from header
            byte[] b = Base64.encodeBase64(title.getBytes());
            String fromHeader = "=?utf-8?b?"+new String(b)+"?= <"+from+">";

            Content bodyText = new Content().withData(bodyHtml);
            Body bodyObj = new Body().withHtml(bodyText);

            // create a message
            Message message = new Message().withSubject(subjectText).withBody(bodyObj);

            SendEmailRequest request = new SendEmailRequest().withSource(fromHeader).withDestination(dest).withMessage(message);

            //send the email
            simpleEmailServiceClient.sendEmail(request);
        } catch (Exception e) {
            logger.error("error on send email",e);
            throw new EmailException("ocorreu um erro ao enviar o email");
        }
    }

    @Async
    @Override
    public void sendSimpleMailText(String destination, String from, String subject, String title, List<String> cc, String bodyText) throws EmailException {
        try {
            Destination dest = new Destination().withBccAddresses(new String[]{destination});
            dest.setCcAddresses(cc);

            Content subjectText = new Content().withData(subject);

            Content body = new Content().withData(bodyText);
            Body bodyObj = new Body().withText(body);

            //from header
            byte[] b = Base64.encodeBase64(title.getBytes());
            String fromHeader = "=?utf-8?b?"+new String(b)+"?= <"+from+">";

            // create a message
            Message message = new Message().withSubject(subjectText).withBody(bodyObj);
            SendEmailRequest request = new SendEmailRequest().withSource(fromHeader).withDestination(dest).withMessage(message);

            //send the email
            simpleEmailServiceClient.sendEmail(request);
        } catch (Exception e) {
            logger.error("error on send email",e);
            throw new EmailException("ocorreu um erro ao enviar o email");
        }
    }
}
