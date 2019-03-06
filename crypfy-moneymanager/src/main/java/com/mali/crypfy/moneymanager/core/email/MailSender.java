package com.mali.crypfy.moneymanager.core.email;

import com.mali.crypfy.moneymanager.core.email.exceptions.EmailException;

import java.util.List;

/**
 * Interface Wich Manage and Courier Emails
 */
public interface MailSender {

    /**
     * Send a Simple Text Email
     * @param destination
     * @param from
     * @param subject
     * @param title
     * @param bodyText
     * @throws EmailException
     */
    public void sendSimpleMailText(String destination, String from, String subject, String title, String bodyText) throws EmailException;

    /**
     * Send a Simple Html Email
     * @param destination
     * @param from
     * @param subject
     * @param title
     * @param bodyHtml
     * @throws EmailException
     */
    public void sendSimpleMailHtml(String destination, String from, String subject, String title,String bodyHtml) throws EmailException;

    /**
     * Send a Simple Html Email with CC
     * @param destination
     * @param from
     * @param subject
     * @param title
     * @param cc
     * @param bodyHtml
     * @throws EmailException
     */
    public void sendSimpleMailHtml(String destination, String from, String subject, String title, List<String> cc, String bodyHtml) throws EmailException;

    /**
     * Send a Simple Text Email With CC
     * @param destination
     * @param from
     * @param subject
     * @param title
     * @param cc
     * @param bodyText
     * @throws EmailException
     */
    public void sendSimpleMailText(String destination, String from, String subject, String title, List<String> cc,String bodyText) throws EmailException;
}
