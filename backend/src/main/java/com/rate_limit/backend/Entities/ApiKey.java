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
    public void onCreate()
    {
        this.createdAt = Instant.now();
    }
    
    public boolean isActive()
    {
        return revokedAt == null;
    }

    public UUID getId()
    {
        return this.id;
    }

    public void setId(UUID id)
    {
        this.id=id;
    }

    public User getUser()
    {
        return this.user;
    }
    
    public void setUser(User user)
    {
        this.user=user;
    }

    public String getKeyHash()
    {
        return this.keyHash;
    }

    public void setKeyHash(String keyHash)
    {
        this.keyHash=keyHash;
    }

    public String getLabel()
    {
        return this.label;
    }

    public void setLabel(String label)
    {
        this.label=label;
    }

    public Instant getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt=createdAt;
    }

    public Instant getRevokedAt()
    {
        return this.revokedAt;
    }

    public void setRevokedAt(Instant revokedAt)
    {
        this.revokedAt=revokedAt;
    }

}
