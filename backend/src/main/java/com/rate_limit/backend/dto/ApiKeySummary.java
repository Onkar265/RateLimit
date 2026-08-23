package com.rate_limit.backend.dto;

import java.util.UUID;
import java.time.Instant;

public record ApiKeySummary(UUID id, String label, Instant createdAt, boolean active) {
}