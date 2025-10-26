package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.event.output.MailOutputEvent;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.service.MailingService;
import com.ourbusway.uaa.service.MessagingOutputService;
import com.ourbusway.uaa.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailingServiceImpl implements MailingService {
    private static final String FULL_NAME = "fullName";
    private static final String EMAIL_ADDRESS = "emailAddress";
    private final MessagingOutputService messagingOutputService;

    @Value("${ui.client.url}")
    public String ourbuswayClientUrl;
    @Value("${ui.admin.url}")
    public String ourbuswayAdminUrl;
    // Constants for query parameters
    @Value("${mail.template.organisation_member_reset_password_confirmation}")
    private String resetPasswordConfirmationTemplateId;
    @Value("${mail.template.organisation_member_reset_password}")
    private String resetPasswordTemplateId;
    @Value("${mail.template.account-validation}")
    private String accountValidationTemplateId;
    @Value("${mail.template.organisation_member_account_confirmation_after_validation}")
    private String organisationMemberAccountConfirmationAfterValidation;

    @Override
    public void sendResetPasswordEmail(UserModel loginModel, TokenModel tokenModel) {
        MailOutputEvent mailOutputEvent = new MailOutputEvent();
        mailOutputEvent.setTo(List.of(loginModel.getEmail()));
        mailOutputEvent.setSubject("OurBusWay - Forgot your password?");
        Map<String, String> variables = new HashMap<>();
        String fullName = loginModel.getFirstName() + " " + loginModel.getLastName();
        variables.put(FULL_NAME, fullName.trim());

        StringBuilder accountValidationLinkBuilder;
        if (SecurityUtil.isAdmin(loginModel)) {
            accountValidationLinkBuilder =
                    new StringBuilder(ourbuswayAdminUrl)
                            .append("/reset-password?token=")
                            .append(tokenModel.getValue());
        } else {
            accountValidationLinkBuilder =
                    new StringBuilder(ourbuswayClientUrl)
                            .append("/reset-password?token=")
                            .append(tokenModel.getValue());
        }
        variables.put("accountValidationLink", accountValidationLinkBuilder.toString());
        variables.put("resetKeyExpDate", tokenModel.getExpirationDate().toString());
        mailOutputEvent.setVariables(variables);
        mailOutputEvent.setTemplateId(resetPasswordTemplateId);
        messagingOutputService.sendMailEvent(mailOutputEvent);
    }

    @Override
    public void sendResetPasswordConfirmationEmail(UserModel user) {
        MailOutputEvent mailOutputEvent = new MailOutputEvent();
        mailOutputEvent.setTo(List.of(user.getEmail()));
        mailOutputEvent.setSubject("OurBusWay - New password confirmation");
        Map<String, String> variables = new HashMap<>();
        String fullName = user.getFirstName() + " " + user.getLastName();
        variables.put(FULL_NAME, fullName.trim());
        variables.put(EMAIL_ADDRESS, user.getEmail());
        mailOutputEvent.setVariables(variables);
        mailOutputEvent.setTemplateId(resetPasswordConfirmationTemplateId);
        messagingOutputService.sendMailEvent(mailOutputEvent);
    }

    @Override
    public void sendActivationAccountConfirmationEmail(UserModel user) {
        MailOutputEvent mailOutputEvent = new MailOutputEvent();
        mailOutputEvent.setTo(List.of(user.getEmail()));
        mailOutputEvent.setSubject("OurBusWay - Account activated");
        Map<String, String> variables = new HashMap<>();
        String fullName = user.getFirstName() + " " + user.getLastName();
        variables.put(FULL_NAME, fullName.trim());
        mailOutputEvent.setVariables(variables);
        mailOutputEvent.setTemplateId(organisationMemberAccountConfirmationAfterValidation);
        messagingOutputService.sendMailEvent(mailOutputEvent);
    }

    @Override
    public void sendActivationAccountEmail(
            UserModel user,
            TokenModel tokenModel) {

        MailOutputEvent mailOutputEvent = new MailOutputEvent();
        mailOutputEvent.setTo(List.of(user.getEmail()));
        mailOutputEvent.setSubject("OurBusWay - Activate your account");
        Map<String, String> variables = new HashMap<>();

        String fullName = user.getFirstName() + " " + user.getLastName();
        variables.put(FULL_NAME, fullName.trim());

        StringBuilder accountValidationLinkBuilder;
        if (SecurityUtil.isAdmin(user)) {
            accountValidationLinkBuilder =
                    new StringBuilder(ourbuswayAdminUrl)
                            .append("/account-validation?token=")
                            .append(tokenModel.getValue());
        } else {
            accountValidationLinkBuilder =
                    new StringBuilder(ourbuswayClientUrl)
                            .append("/account-validation?token=")
                            .append(tokenModel.getValue());
        }
        variables.put("accountValidationLink", accountValidationLinkBuilder.toString());
        variables.put("resetKeyExpDate", tokenModel.getExpirationDate().toString());
        mailOutputEvent.setVariables(variables);
        mailOutputEvent.setTemplateId(accountValidationTemplateId);
        messagingOutputService.sendMailEvent(mailOutputEvent);
    }
}
