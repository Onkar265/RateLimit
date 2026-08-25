package com.rate_limit.backend.service;

import com.rate_limit.backend.dto.CreateApiKeyResponse;
import com.rate_limit.backend.entities.ApiKey;
import com.rate_limit.backend.entities.User;
import com.rate_limit.backend.exception.ApiKeyNotFoundException;
import com.rate_limit.backend.repositories.ApiKeyRepository;
import com.rate_limit.backend.repositories.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final StringRedisTemplate redis;

    public ApiKeyService(ApiKeyRepository apiKeyRepository, UserRepository userRepository, StringRedisTemplate redis) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
        this.redis = redis;
    }

    public CreateApiKeyResponse createKey(UUID userId, String label) {
        String rawKey = generateRawKey();
        String keyHash = sha256(rawKey);

        User user = userRepository.getReferenceById(userId);

        ApiKey key = new ApiKey();
        key.setUser(user);
        key.setKeyHash(keyHash);
        key.setLabel(label);
        apiKeyRepository.save(key);

        return new CreateApiKeyResponse(key.getId(), rawKey, label);
    }

    public void revokeKey(UUID userId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId)
            .orElseThrow(() -> new ApiKeyNotFoundException(keyId));

        if (!key.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("not your key");
        }

        key.setRevokedAt(Instant.now());
        apiKeyRepository.save(key);

        redis.delete("ratelimit:planlimit:" + key.getKeyHash());
    }

    private String generateRawKey() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return "sk_" + HexFormat.of().formatHex(bytes);
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}