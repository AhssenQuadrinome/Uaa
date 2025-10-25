package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.resource.auth.*;
import com.ourbusway.uaa.dao.service.TokenDaoService;
import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.TokenSpecification;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.enumeration.TokenTypeEnum;
import com.ourbusway.uaa.exception.AuthenticationUnauthorizedException;
import com.ourbusway.uaa.exception.AuthorizationForbiddenException;
import com.ourbusway.uaa.exception.ResourceNotFoundException;
import com.ourbusway.uaa.exception.enumeration.AuthenticationUnauthorizedExceptionTitleEnum;
import com.ourbusway.uaa.exception.enumeration.AuthorizationForbiddenExceptionTitleEnum;
import com.ourbusway.uaa.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.service.AuthenticationService;
import com.ourbusway.uaa.service.JwtTokenProviderService;
import com.ourbusway.uaa.service.MailingService;
import com.ourbusway.uaa.service.TokenService;
import com.ourbusway.uaa.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDaoService credentialDaoService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProviderService jwtTokenProviderService;
    private final TokenService tokenService;
    private final MailingService mailingService;
    private final TokenDaoService tokenDaoService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<TokenGetResource> authenticate(
            UserCredentialsPostResource userCredentialsPostResource) {
        Optional<UserModel> optionalLogin = Optional.empty();
        try {
            optionalLogin =
                    Optional.of(
                            credentialDaoService.findOneBy(
                                    UserSpecification.withEmail(userCredentialsPostResource.getUsername())));
            log.info("Found user with email: {}", userCredentialsPostResource.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentialsPostResource.getUsername(),
                            userCredentialsPostResource.getPassword()));
        } catch (BadCredentialsException | ResourceNotFoundException exception) {
            log.warn(
                    "Bad credentials or users not found with username: {}",
                    userCredentialsPostResource.getUsername());

            throw new AuthenticationUnauthorizedException(
                    AuthenticationUnauthorizedExceptionTitleEnum.NOT_AUTHORIZED,
                    "Invalid credentials. Please try again.");
        }
        UserModel login = optionalLogin.get();

        final String jwtToken = jwtTokenProviderService.generateToken(login);
        TokenGetResource tokenGetResource = new TokenGetResource();
        tokenGetResource.setAccessToken(jwtToken);
        tokenGetResource.setExpiresIn(
                jwtTokenProviderService.getExpirationDateFromToken(jwtToken).toInstant().getEpochSecond());
        return ResponseEntity.ok(tokenGetResource);
    }

    @Override
    public ResponseEntity<Void> forgotPassword(
            UserForgotPasswordPostResource userForgotPasswordPostResource) {
        try {
            UserModel user =
                    credentialDaoService.findOneBy(
                            UserSpecification.withEmail(userForgotPasswordPostResource.getEmail()));
            log.debug(
                    "Found user {} {} with corresponding email: {}",
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    userForgotPasswordPostResource.getEmail());
            log.debug("Will generate a reset key and send it by email ...");
            TokenModel passwordResetToken = tokenService.generatePasswordToken(user);
            mailingService.sendResetPasswordEmail(user, passwordResetToken);
        } catch (Exception ignored) {
            log.debug(
                    "No user found with email: {}. No response will be returned to the user",
                    userForgotPasswordPostResource.getEmail());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> resetPassword(
            UserResetPasswordPostResource userResetPasswordPostResource) {
        TokenModel tokenModel;
        UserModel loginModel;
        try {
            log.debug("retrieve user based on password reset key");
            String encryptedToken = userResetPasswordPostResource.getResetPasswordKey();
            tokenModel =
                    tokenDaoService.findOneBy(
                            TokenSpecification.withEncryptedToken(encryptedToken)
                                    .and(TokenSpecification.withType(TokenTypeEnum.PASSWORD_RESET)));
            loginModel = tokenModel.getLogin();
            log.debug(
                    "Found user {} {} with corresponding reset key",
                    loginModel.getId(),
                    loginModel.getFirstName(),
                    loginModel.getLastName());
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.RESET_KEY_NOT_FOUND,
                    "The provided password reset key is invalid or was not found.");
        }
        // check resetKey expiration date
        if (!LocalDateTime.now().isBefore(tokenModel.getExpirationDate())) {
            log.debug(
                    "Reset password key has expired. Expiration date was for: {}. Will generate a new one and send it by email",
                    tokenModel.getExpirationDate());
            tokenDaoService.delete(tokenModel);
            tokenModel = tokenService.generatePasswordToken(loginModel);

            // send email with new key
            mailingService.sendResetPasswordEmail(loginModel, tokenModel);
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.RESET_KEY_EXPIRED,
                    "The password reset key has expired. A new reset password link will be sent to your registered email address.");
        }
        tokenDaoService.delete(tokenModel);
        loginModel.setPassword(
                passwordEncoder.encode(userResetPasswordPostResource.getPassword()));
        loginModel = credentialDaoService.save(loginModel);
        mailingService.sendResetPasswordConfirmationEmail(loginModel);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> validateAccount(
            UserValidateAccountPostResource userValidateAccountPostResource) {

        UserModel loginModel;
        TokenModel tokenModel;
        try {
            log.debug("retrieve user based on account validation reset key");
            String encryptedToken = userValidateAccountPostResource.getActivationKey();
            tokenModel =
                    tokenDaoService.findOneBy(
                            TokenSpecification.withEncryptedToken(encryptedToken)
                                    .and(TokenSpecification.withType(TokenTypeEnum.ACCOUNT_VALIDATION)));
            loginModel = tokenModel.getLogin();
            log.debug(
                    "Found user {} {} with corresponding account validation key",
                    loginModel.getId(),
                    loginModel.getFirstName(),
                    loginModel.getLastName());
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.RESET_KEY_NOT_FOUND,
                    "The provided account validation key is invalid or was not found.");
        }
        // check resetKey expiration date
        if (!LocalDateTime.now().isBefore(tokenModel.getExpirationDate())) {
            log.debug(
                    "Account validation key has expired. Expiration date was for: {}. Will generate a new one and send it by email",
                    tokenModel.getExpirationDate());
            tokenDaoService.delete(tokenModel);
            tokenModel = tokenService.generateAccountValidationToken(loginModel);
            mailingService.sendActivationAccountEmail(loginModel, tokenModel);
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.RESET_KEY_EXPIRED,
                    "The account validation key has expired. A new account validation link will be sent to your registered email address.");
        }
        tokenDaoService.delete(tokenModel);
        loginModel.setActivated(true);

        if ((loginModel.getPassword() == null || loginModel.getPassword().isBlank())
                && userValidateAccountPostResource.getPassword() != null
                && !userValidateAccountPostResource.getPassword().isBlank()) {
            loginModel.setPassword(passwordEncoder.encode(userValidateAccountPostResource.getPassword()));
        }

        loginModel = credentialDaoService.save(loginModel);
        mailingService.sendActivationAccountConfirmationEmail(loginModel);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> validateToken(
            TokenValidationPostResource tokenValidationPostResource) {
        if (!jwtTokenProviderService.validateToken(tokenValidationPostResource.getToken())) {
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.TOKEN_NOT_VALID, "Token used is not valid");
        }
        // necessary for recent devices history
        Claims claims = jwtTokenProviderService.getClaimsFromToken(tokenValidationPostResource.getToken());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TokenGetResource> refreshToken(
            RefreshTokenPostResource refreshTokenPostResource) {
        TokenValidationPostResource tokenValidationPostResource = new TokenValidationPostResource();
        tokenValidationPostResource.setToken(refreshTokenPostResource.getRefreshToken());
        this.validateToken(tokenValidationPostResource);
        String userId =
                jwtTokenProviderService.getClaimFromTokenEvenIfExpired(
                        tokenValidationPostResource.getToken(), SecurityUtil.IDENTIFIER_KEY);

        UserModel user = credentialDaoService.findOneBy(UserSpecification.withUuid(userId));
        if (!user.isEnabled()) {
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.USER_DISABLED,
                    "Your account is disabled. Please contact support.");
        }

        if (!user.isActivated()) {
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.USER_NOT_ACTIVATED,
                    "Your account is not yet validated. Please check your email for validation instructions.");
        }

        final String jwtToken = jwtTokenProviderService.generateToken(user);

        TokenGetResource tokenGetResource = new TokenGetResource();
        tokenGetResource.setAccessToken(jwtToken);
        tokenGetResource.setExpiresIn(
                jwtTokenProviderService.getExpirationDateFromToken(jwtToken).toInstant().getEpochSecond());
        return ResponseEntity.ok(tokenGetResource);
    }

}

