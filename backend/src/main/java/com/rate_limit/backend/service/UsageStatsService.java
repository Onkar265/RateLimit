package com.rate_limit.backend.service;

import com.rate_limit.backend.dto.UsageLogEntry;
import com.rate_limit.backend.repositories.UsageLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UsageStatsService {

    private final UsageLogRepository usageLogRepository;

    public UsageStatsService(UsageLogRepository usageLogRepository) {
        this.usageLogRepository = usageLogRepository;
    }

    public List<UsageLogEntry> recentUsage(UUID userId) {
        return usageLogRepository.findRecentUsageByUserId(userId, PageRequest.of(0, 50));
    }
}