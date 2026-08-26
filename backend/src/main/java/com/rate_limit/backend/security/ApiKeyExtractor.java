package com.rate_limit.backend.security;

import com.rate_limit.backend.exception.MissingApiKeyException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class ApiKeyExtractor {

    private static final String HEADER_NAME = "X-API-Key";

    public String extract(HttpServletRequest request) {
        String rawKey = request.getHeader(HEADER_NAME);

        if (rawKey == null || rawKey.isBlank()) {
            throw new MissingApiKeyException();
        }

        return hash(rawKey);
    }

    private String hash(String rawKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}