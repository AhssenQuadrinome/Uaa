package com.ourbusway.uaa.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
public class InternalServiceAuthFilter implements Filter {

    @Value("${internal.service-secret}")
    private String internalSecret;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;

        log.info("🌐 Incoming request: {} {}", http.getMethod(), http.getRequestURI());

        if (http.getRequestURI().startsWith("/internal")) {
            String receivedSecret = http.getHeader("X-Internal-Secret");

            log.info("🔍 Checking internal secret...");
            log.info("🔍 Received header X-Internal-Secret: {}", receivedSecret);
            log.info("🔍 Expected internal secret: {}", internalSecret);

            if (receivedSecret == null) {
                log.error("❌ Missing X-Internal-Secret header → rejecting");
                ((jakarta.servlet.http.HttpServletResponse) response)
                        .sendError(403, "Forbidden: Missing internal secret");
                return;
            }

            if (!receivedSecret.equals(internalSecret)) {
                log.error("❌ Invalid internal secret! Header={}, Expected={}",
                        receivedSecret, internalSecret);
                ((jakarta.servlet.http.HttpServletResponse) response)
                        .sendError(403, "Forbidden: Invalid internal secret");
                return;
            }

            log.info("✅ Internal secret validated successfully");
        }

        chain.doFilter(request, response);
    }
}
