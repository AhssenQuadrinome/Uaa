package com.ourbusway.uaa.controller.impl;

import com.ourbusway.uaa.controller.AuthController;
import com.ourbusway.uaa.resource.auth.*;
import com.ourbusway.uaa.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationService authenticationService;

    public ResponseEntity<TokenGetResource> postCredentials(
            UserCredentialsPostResource userCredentialsPostResource) {
        return authenticationService.authenticate(userCredentialsPostResource);
    }

    @Override
    public ResponseEntity<Void> postEmailToInitiateForgotPassword(
            UserForgotPasswordPostResource userForgotPasswordPostResource) {
        return authenticationService.forgotPassword(userForgotPasswordPostResource);
    }

    @Override
    public ResponseEntity<Void> resetPassword(
            UserResetPasswordPostResource userResetPasswordPostResource) {
        return authenticationService.resetPassword(userResetPasswordPostResource);
    }

    @Override
    public ResponseEntity<Void> validateAccount(
            UserValidateAccountPostResource userValidateAccountPostResource) {
        return authenticationService.validateAccount(userValidateAccountPostResource);
    }

    @Override
    public ResponseEntity<Void> validateToken(
            TokenValidationPostResource tokenValidationPostResource) {
        return authenticationService.validateToken(tokenValidationPostResource);
    }

    @Override
    public ResponseEntity<TokenGetResource> refreshToken(
            RefreshTokenPostResource refreshTokenPostResource) {
        return authenticationService.refreshToken(refreshTokenPostResource);
    }

}
