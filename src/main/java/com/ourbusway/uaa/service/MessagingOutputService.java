package com.ourbusway.uaa.service;


import com.ourbusway.uaa.event.output.EmailConfirmedOutputEvent;
import com.ourbusway.uaa.event.output.MailOutputEvent;

public interface MessagingOutputService {

    void sendMailEvent(MailOutputEvent mailOutputEvent);

    void sendEmailConfirmedEvent(EmailConfirmedOutputEvent emailConfirmedOutputEvent);
}
