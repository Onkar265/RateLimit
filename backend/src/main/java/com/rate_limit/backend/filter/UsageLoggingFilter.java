package com.rate_limit.backend.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import com.rate_limit.backend.service.UsageLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UsageLoggingFilter extends OncePerRequestFilter{

    private final UsageLogService usageLogService;
    
    public UsageLoggingFilter(UsageLogService usageLogService) {
        this.usageLogService = usageLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throws ServletException, IOException{
        long start = System.nanoTime();
        try
        {
            chain.doFilter(req, res);
        }
        finally
        {
            String apiKeyId = (String)req.getAttribute("apiKeyId");
            int responseTimeMs = (int)(System.nanoTime()-start);
            usageLogService.record(
                apiKeyId,
                req.getRequestURI(),
                req.getMethod(),
                res.getStatus(),
                responseTimeMs
            );
        }
    }

}
