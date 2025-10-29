package com.ourbusway.uaa.security;

import com.ourbusway.uaa.exception.AuthenticationUnauthorizedException;
import com.ourbusway.uaa.exception.enumeration.AuthenticationUnauthorizedExceptionTitleEnum;
import com.ourbusway.uaa.service.JwtTokenProviderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.User;


import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProviderService jwtTokenProviderService;
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.substring(TOKEN_PREFIX.length());
            try {
                Claims claims = jwtTokenProviderService.getClaimsFromToken(token);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                var authority = new SimpleGrantedAuthority("ROLE_" + role);
                User userDetails = new User(email, "", java.util.List.of(authority));
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Authenticated user {} with role {}", email, role);

            } catch (ExpiredJwtException e) {
                throw new AuthenticationUnauthorizedException(
                        AuthenticationUnauthorizedExceptionTitleEnum.EXPIRED_TOKEN,
                        "Your token has expired");
            } catch (JwtException e) {
                log.warn("Invalid JWT: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match("/actuator/**", request.getServletPath())
                || matcher.match("/v3/**", request.getServletPath());
    }
}
