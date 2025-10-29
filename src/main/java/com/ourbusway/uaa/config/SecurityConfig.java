package com.ourbusway.uaa.config;

import com.ourbusway.uaa.security.JwtAuthenticationFilter;
import com.ourbusway.uaa.service.LocalUserDetailsService;
import com.ourbusway.uaa.service.JwtTokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final ObjectProvider<LocalUserDetailsService> customUserDetailsServiceProvider;
    private final JwtTokenProviderService jwtTokenProviderService;
    private final PasswordEncoder passwordEncoder; // injected from AppConfig

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        LocalUserDetailsService userDetailsService = customUserDetailsServiceProvider.getIfAvailable();
        if (userDetailsService == null) {
            throw new IllegalStateException("LocalUserDetailsService bean not available");
        }

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/login",
                                "/reset-password",
                                "/refresh-token",
                                "/validate-account",
                                "/forgot-password",
                                "/validate-token",
                                "/users/register",
                                "/actuator/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs"
                        ).permitAll()

                        // Admin endpoints
                        .requestMatchers("/users/admin/**").hasRole("ADMINISTRATOR")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProviderService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}