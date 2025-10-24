package com.ourbusway.uaa.service;

import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;

public interface MailingService {

    void sendResetPasswordEmail(UserModel loginModel, TokenModel tokenModel);

    void sendResetPasswordConfirmationEmail(UserModel loginModel);

    void sendActivationAccountConfirmationEmail(UserModel loginModel);

    void sendActivationAccountEmail(
            UserModel user, TokenModel tokenModel);

}
