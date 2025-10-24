package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.dao.service.TokenDaoService;
import com.ourbusway.uaa.enumeration.TokenTypeEnum;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.service.TokenService;
import com.ourbusway.uaa.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenDaoService tokenDaoService;
    private final PasswordEncoder passwordEncoder;

    @Value("${uaa.security.account.activation-key-life}")
    private int activationKeyLife;

    @Value("${uaa.security.password.reset-key-life}")
    private int passwordResetKeyLife;

    @Override
    public TokenModel generatePasswordToken(UserModel user) {
        TokenModel token = new TokenModel();
        token.setLogin(user);
        final LocalDateTime now = LocalDateTime.now();
        token.setExpirationDate(now.plusDays(passwordResetKeyLife));
        token.setValue(SecurityUtil.generateKey());
        token.setType(TokenTypeEnum.PASSWORD_RESET);
        token = tokenDaoService.save(token);
        return token;
    }

    @Override
    public TokenModel generateAccountValidationToken(UserModel user) {
        TokenModel token = new TokenModel();
        token.setLogin(user);
        final LocalDateTime now = LocalDateTime.now();
        token.setExpirationDate(now.plusDays(activationKeyLife));
        token.setValue(SecurityUtil.generateKey());
        token.setType(TokenTypeEnum.ACCOUNT_VALIDATION);
        return tokenDaoService.save(token);
    }
}
