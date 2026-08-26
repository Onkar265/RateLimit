package com.rate_limit.backend.filter;

import com.rate_limit.backend.exception.InvalidApiKeyException;
import com.rate_limit.backend.exception.MissingApiKeyException;
import com.rate_limit.backend.exception.RevokedApiKeyException;
import com.rate_limit.backend.security.ApiKeyExtractor;
import com.rate_limit.backend.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;
    private final ApiKeyExtractor apiKeyExtractor;

    public RateLimitFilter(RateLimiterService rateLimiterService, ApiKeyExtractor apiKeyExtractor) {
        this.rateLimiterService = rateLimiterService;
        this.apiKeyExtractor = apiKeyExtractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String apiKeyId;
        try {
            apiKeyId = apiKeyExtractor.extract(req);
            req.setAttribute("apiKeyId", apiKeyId);
        } catch (MissingApiKeyException e) {
            writeError(res, 401, "missing or invalid API key");
            return;
        }

        try {
            if (!rateLimiterService.tryConsume(apiKeyId)) {
                writeError(res, 429, "rate limit exceeded");
                return;
            }
        } catch (InvalidApiKeyException | RevokedApiKeyException e) {
            writeError(res, 401, "invalid or revoked API key");
            return;
        }

        chain.doFilter(req, res);
    }

    private void writeError(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}