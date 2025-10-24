package com.ourbusway.uaa.service;

import com.ourbusway.uaa.resource.auth.*;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<TokenGetResource> authenticate(
            UserCredentialsPostResource userCredentialsPostResource);

    ResponseEntity<Void> forgotPassword(
            UserForgotPasswordPostResource userForgotPasswordPostResource);

    ResponseEntity<Void> resetPassword(UserResetPasswordPostResource userResetPasswordPostResource);

    ResponseEntity<Void> validateAccount(
            UserValidateAccountPostResource userValidateAccountPostResource);

    ResponseEntity<Void> validateToken(TokenValidationPostResource tokenValidationPostResource);

    ResponseEntity<TokenGetResource> refreshToken(RefreshTokenPostResource refreshTokenPostResource);

}
