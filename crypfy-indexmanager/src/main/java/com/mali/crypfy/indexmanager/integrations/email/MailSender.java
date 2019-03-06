package com.mali.crypfy.indexmanager.integrations.email;

import com.mali.crypfy.indexmanager.integrations.email.exception.EmailException;

import java.util.List;

public interface MailSender {
    public void sendSimpleMailText(String destination, String from, String subject, String title, String bodyText) throws EmailException;
    public void sendSimpleMailHtml(String destination, String from, String subject, String title,String bodyHtml) throws EmailException;
    public void sendSimpleMailHtml(String destination, String from, String subject, String title, List<String> cc, String bodyHtml) throws EmailException;
    public void sendSimpleMailText(String destination, String from, String subject, String title, List<String> cc,String bodyText) throws EmailException;
}
