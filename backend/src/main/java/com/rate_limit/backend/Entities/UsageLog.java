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
    public void onCreate() {
        this.requestedAt = Instant.now();
    }

    public ApiKey getApiKey()
    {
        return this.apiKey;
    }

    public String getEndPoint()
    {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint=endpoint;
    }

    public String getMethod()
    {
        return this.method;
    }

    public void setMethod(String method)
    {
        this.method=method;
    }

    public int getStatusCode()
    {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode=statusCode;
    }

    public int getResponseTimeMs()
    {
        return this.responseTimeMs;
    }

    public void setResponseTimeMs(int responseTimeMs)
    {
        this.responseTimeMs=responseTimeMs;
    }

    public Instant getRequestedAt()
    {
        return this.requestedAt;
    }

    public void setRequestedAt(Instant requestedAt)
    {
        this.requestedAt=requestedAt;
    }

    public UUID getId()
    {
        return this.id;
    }

    public void setId(UUID id)
    {
        this.id=id;
    }
}
    
