package com.rate_limit.backend.service;

import com.rate_limit.backend.dto.UsageLogEntry;
import com.rate_limit.backend.repositories.UsageLogRepository;
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
        return usageLogRepository.findTop50ByApiKey_User_IdOrderByRequestedAtDesc(userId).stream()
            .map(log -> new UsageLogEntry(
                log.getEndPoint(),
                log.getMethod(),
                log.getStatusCode(),
                log.getResponseTimeMs(),
                log.getRequestedAt()
            ))
            .toList();
    }
}