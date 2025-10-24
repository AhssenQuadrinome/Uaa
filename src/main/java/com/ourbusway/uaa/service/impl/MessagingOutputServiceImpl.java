package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.event.output.EmailConfirmedOutputEvent;
import com.ourbusway.uaa.event.output.MailOutputEvent;
import com.ourbusway.uaa.service.MessagingOutputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingOutputServiceImpl implements MessagingOutputService {

    private final StreamBridge streamBridge;
    @Value("${spring.cloud.stream.exchange.send-email}")
    String mailExchange;
    @Value("${spring.cloud.stream.exchange.email-confirmed}")
    String emailConfirmedExchange;

    @Async
    @Override
    public void sendMailEvent(MailOutputEvent mailOutputEvent) {
        log.debug(
                "Sending event to exchange: <{}> with payload: {} ...", mailExchange, mailOutputEvent);
        streamBridge.send(mailExchange, mailOutputEvent);
        log.info("Mail event sent");
    }

    @Override
    public void sendEmailConfirmedEvent(EmailConfirmedOutputEvent emailConfirmedOutputEvent) {
        log.debug(
                "Sending event to exchange: <{}> with payload: {} ...", emailConfirmedExchange, emailConfirmedOutputEvent);
        streamBridge.send(emailConfirmedExchange, emailConfirmedOutputEvent);
        log.info("Email confirmed event sent");
    }
}
