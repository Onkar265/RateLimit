package com.rate_limit.backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redis;
    private final RedisScript<Long> tokenBucketScript;
    private final PlanLimitResolver planLimitResolver;

    private static final int REFILL_RATE_PER_SECOND = 1;

    public RateLimiterService(StringRedisTemplate redis,
                               RedisScript<Long> tokenBucketScript,
                               PlanLimitResolver planLimitResolver) {
        this.redis = redis;
        this.tokenBucketScript = tokenBucketScript;
        this.planLimitResolver = planLimitResolver;
    }

    public boolean tryConsume(String apiKeyId) {
        int capacity = planLimitResolver.resolveDailyLimit(apiKeyId);
        String bucketKey = "ratelimit:" + apiKeyId + ":tokenbucket";
        long now = Instant.now().getEpochSecond();

        Long result = redis.execute(
            tokenBucketScript,
            List.of(bucketKey),
            String.valueOf(capacity),
            String.valueOf(REFILL_RATE_PER_SECOND),
            String.valueOf(now)
        );

        return result != null && result == 1L;
    }
}