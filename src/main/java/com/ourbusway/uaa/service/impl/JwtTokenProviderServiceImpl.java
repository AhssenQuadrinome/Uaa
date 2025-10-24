package com.ourbusway.uaa.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.service.JwtTokenProviderService;
import com.ourbusway.uaa.util.DefaultClock;
import com.ourbusway.uaa.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ourbusway.uaa.util.SecurityUtil.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Getter
@Setter
public class JwtTokenProviderServiceImpl implements JwtTokenProviderService {

    public static final Clock clock = DefaultClock.INSTANCE;
    private final UserDaoService credentialDaoService;

    @Value("${uaa.security.access-token.expiration}")
    public Long expiration;

    @Value("${uaa.security.access-token.key}")
    private String secret;

    @Override
    public Date getExpirationDateFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        }
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        token = token.replace(SecurityUtil.TOKEN_TYPE, "").trim();
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    @Override
    public String getClaimFromTokenEvenIfExpired(String token, String claimName) {
        if (this.getClaimsFromTokenEvenIfExpired(token).getClaim(claimName).isMissing()) {
            return null;
        } else {
            return this.getClaimsFromTokenEvenIfExpired(token).getClaim(claimName).asString();
        }
    }

    @Override
    public DecodedJWT getClaimsFromTokenEvenIfExpired(String token) {
        token = token.replace(SecurityUtil.TOKEN_TYPE, "").trim();
        return JWT.decode(token);
    }

    @Override
    public String generateToken(UserModel user) {
        log.debug("Generating JWT for user: {}", user.getEmail());

        final Date createdDate = clock.now();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put(IDENTIFIER_KEY, user.getId().toString());
        claims.put(USERNAME_KEY, user.getEmail());
        claims.put("role", user.getRole().name());

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .expiration(expirationDate)
                .issuedAt(createdDate)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        token = token.replace(SecurityUtil.TOKEN_TYPE, "").trim();
        try {
            if (!isTokenExpired(token)) {
                Claims claims = getClaimsFromToken(token);
                String userId = claims.get(IDENTIFIER_KEY).toString();
                return credentialDaoService.existsBy(
                        UserSpecification.withUuid(userId)
                                .and(UserSpecification.withEnabled(true))
                                .and(UserSpecification.withActive(true)));
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Date calculateExpirationDate(Date createdDate, LocalDate accountEndDate) {
        Date defaultExpiration = new Date(createdDate.getTime() + expiration * 1000);

        if (accountEndDate == null) {
            return defaultExpiration;
        }

        Date accountExpiration = Date.from(accountEndDate.atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault())
                .toInstant());

        return accountExpiration.before(defaultExpiration) ? accountExpiration : defaultExpiration;
    }

    public boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(clock.now());
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
