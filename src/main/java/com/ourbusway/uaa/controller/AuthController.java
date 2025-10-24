package com.ourbusway.uaa.controller;

import com.ourbusway.uaa.resource.auth.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<TokenGetResource> postCredentials(
            @RequestBody @Valid UserCredentialsPostResource userCredentialsPostResource);

    @PostMapping("/forgot-password")
    ResponseEntity<Void> postEmailToInitiateForgotPassword(
            @Valid @RequestBody UserForgotPasswordPostResource userForgotPasswordPostResource);

    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(
            @Valid @RequestBody UserResetPasswordPostResource userResetPasswordPostResource);

    @PostMapping("/validate-account")
    ResponseEntity<Void> validateAccount(
            @Valid @RequestBody UserValidateAccountPostResource userValidateAccountPostResource);

    @PostMapping("/validate-token")
    ResponseEntity<Void> validateToken(
            @RequestBody TokenValidationPostResource tokenValidationPostResource);

    @PostMapping("/refresh-token")
    ResponseEntity<TokenGetResource> refreshToken(@Valid @RequestBody RefreshTokenPostResource refreshTokenPostResource);

}
