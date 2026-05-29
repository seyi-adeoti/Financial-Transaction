package com.oluwaseyi.in.Moneymanager.config;

import com.oluwaseyi.in.Moneymanager.entity.AuditLog;
import com.oluwaseyi.in.Moneymanager.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.time.Instant;

@Component
public class AuditFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuditFilter.class);

    private final AuditService auditService;

    @Autowired
    public AuditFilter(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        StatusCaptureResponseWrapper wrapped = new StatusCaptureResponseWrapper(response);

        try {
            filterChain.doFilter(request, wrapped);
        } finally {
            long duration = System.currentTimeMillis() - start;
            Integer status = wrapped.getStatus();
            String username = extractUsername();
            String method = request.getMethod();
            String path = request.getRequestURI();
            String remoteAddr = request.getRemoteAddr();

            AuditLog logEntry = new AuditLog(Instant.now(), method, path, username, status, duration, remoteAddr);
            try {
                auditService.save(logEntry);
            } catch (Exception e) {
                // never let audit failures break the request
                log.warn("Failed to persist audit log", e);
            }
        }
    }

    private String extractUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                return principal == null ? null : principal.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static class StatusCaptureResponseWrapper extends HttpServletResponseWrapper {
        private int status = 200;

        StatusCaptureResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void setStatus(int sc) {
            super.setStatus(sc);
            this.status = sc;
        }

        @Override
        public void sendError(int sc) throws IOException {
            super.sendError(sc);
            this.status = sc;
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            super.sendError(sc, msg);
            this.status = sc;
        }

        @Override
        public int getStatus() {
            return this.status;
        }
    }
}
