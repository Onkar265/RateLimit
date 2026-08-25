package com.rate_limit.backend.service;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.rate_limit.backend.entities.ApiKey;
import com.rate_limit.backend.repositories.ApiKeyRepository;
import com.rate_limit.backend.exception.InvalidApiKeyException;
import java.time.Duration;

@Service
public class PlanLimitResolver {
    
    private final ApiKeyRepository apiKeyRepository;
    private final StringRedisTemplate redis;
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    public PlanLimitResolver(ApiKeyRepository apiKeyRepository,StringRedisTemplate redis)
    {
        this.apiKeyRepository=apiKeyRepository;
        this.redis=redis;
    }

    public int resolveDailyLimit(String apiKeyHash)
    {
        String cacheKey = "ratelimit:planlimit:" + apiKeyHash;
        String cache = redis.opsForValue().get(cacheKey);
        if(cache!=null)
            return Integer.parseInt(cache);

        ApiKey key = apiKeyRepository.findByKeyHashWithPlan(apiKeyHash).orElseThrow(() -> new InvalidApiKeyException(apiKeyHash));

        int limit=key.getUser().getPlan().getDailyLimit();

        redis.opsForValue().set(cacheKey,String.valueOf(limit),CACHE_TTL);

        return limit;
    }
}
