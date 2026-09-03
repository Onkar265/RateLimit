package com.rate_limit.backend.controller;

import com.rate_limit.backend.dto.UsageLogEntry;
import com.rate_limit.backend.security.UserPrincipal;
import com.rate_limit.backend.service.UsageStatsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UsageController {

    private final UsageStatsService usageStatsService;

    public UsageController(UsageStatsService usageStatsService) {
        this.usageStatsService = usageStatsService;
    }

    @GetMapping("/api/usage")
    public List<UsageLogEntry> recentUsage(@AuthenticationPrincipal UserPrincipal principal) {
        return usageStatsService.recentUsage(principal.getUserId());
    }
}