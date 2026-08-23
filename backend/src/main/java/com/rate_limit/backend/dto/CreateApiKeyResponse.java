package com.rate_limit.backend.dto;

import java.util.UUID;

public record CreateApiKeyResponse(UUID id, String rawKey, String label) {
}
