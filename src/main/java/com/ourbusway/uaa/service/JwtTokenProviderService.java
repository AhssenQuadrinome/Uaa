package com.ourbusway.uaa.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ourbusway.uaa.model.UserModel;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtTokenProviderService {

    Date getExpirationDateFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    Claims getClaimsFromToken(String token);

    String getClaimFromTokenEvenIfExpired(String token, String claimName);

    DecodedJWT getClaimsFromTokenEvenIfExpired(String token);

    String generateToken(UserModel user);

    boolean validateToken(String token);
}
