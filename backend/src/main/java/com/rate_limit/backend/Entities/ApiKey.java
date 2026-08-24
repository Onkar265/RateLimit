package com.rate_limit.backend.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "ApiKeys")
public class ApiKey {
    
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "key_hash", nullable = false)
    private String keyHash;

    @Column(nullable = false)
    private String label;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt;

    @PrePersist
    void onCreate()
    {
        this.createdAt = Instant.now();
    }
    
    public boolean isActive()
    {
        return revokedAt == null;
    }

    UUID getId()
    {
        return this.id;
    }

    void setId(UUID id)
    {
        this.id=id;
    }
    
    String getKeyHash()
    {
        return this.keyHash;
    }

    void setKeyHash(String keyHash)
    {
        this.keyHash=keyHash;
    }

    String getLabel()
    {
        return this.label;
    }

    void setLabel(String label)
    {
        this.label=label;
    }

    Instant getCreatedAt()
    {
        return this.createdAt;
    }

    void setCreatedAt(Instant createdAt)
    {
        this.createdAt=createdAt;
    }

    Instant getRevokedAt()
    {
        return this.revokedAt;
    }

    void setRevokedAt(Instant revokedAt)
    {
        this.revokedAt=revokedAt;
    }

}
