package com.rate_limit.backend.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "usage_logs")
public class UsageLog {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private String method;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @Column(name = "response_time_ms", nullable = false)
    private int responseTimeMs;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private Instant requestedAt;

    @PrePersist
    void onCreate() {
        this.requestedAt = Instant.now();
    }

    String getEndPoint()
    {
        return this.endpoint;
    }

    void setEndpoint(String endpoint)
    {
        this.endpoint=endpoint;
    }

    String getMethod()
    {
        return this.method;
    }

    void setMethod(String method)
    {
        this.method=method;
    }

    int getStatusCode()
    {
        return this.statusCode;
    }

    void setStatusCode(int statusCode)
    {
        this.statusCode=statusCode;
    }

    int getResponseTimeMs()
    {
        return this.responseTimeMs;
    }

    void setResponseTimeMs(int responseTimeMs)
    {
        this.responseTimeMs=responseTimeMs;
    }

    Instant getRequestedAt()
    {
        return this.requestedAt;
    }

    void setRequestedAt(Instant requestedAt)
    {
        this.requestedAt=requestedAt;
    }

    UUID getId()
    {
        return this.id;
    }

    void setId(UUID id)
    {
        this.id=id;
    }
}
    
