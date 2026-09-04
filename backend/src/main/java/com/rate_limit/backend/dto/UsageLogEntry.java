package com.rate_limit.backend.dto;

import java.time.Instant;

public record UsageLogEntry(String keyLabel, String endpoint, String method, int statusCode, int responseTimeMs, Instant requestedAt) {
}