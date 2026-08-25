package com.rate_limit.backend.service;

import com.rate_limit.backend.entities.ApiKey;
import com.rate_limit.backend.entities.UsageLog;
import com.rate_limit.backend.repositories.ApiKeyRepository;
import com.rate_limit.backend.repositories.UsageLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsageLogService {

    private final UsageLogRepository usageLogRepository;
    private final ApiKeyRepository apiKeyRepository;
    private static final Logger log = LoggerFactory.getLogger(UsageLogService.class);

    public UsageLogService(UsageLogRepository usageLogRepository, ApiKeyRepository apiKeyRepository) {
        this.usageLogRepository = usageLogRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Async("usageLogExecutor")
    public void record(String apiKeyId, String endpoint, String method, int statusCode, int responseTimeMs) {
        if (apiKeyId == null) {
            return;
        }

        try {
            Optional<ApiKey> keyOpt = apiKeyRepository.findByKeyHash(apiKeyId);
            if (keyOpt.isEmpty()) {
                return;
            }

            UsageLog entry = new UsageLog();
            entry.setApiKey(keyOpt.get());
            entry.setEndpoint(endpoint);
            entry.setMethod(method);
            entry.setStatusCode(statusCode);
            entry.setResponseTimeMs(responseTimeMs);

            usageLogRepository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to write usage log for key {}", apiKeyId, e);
        }
    }
}