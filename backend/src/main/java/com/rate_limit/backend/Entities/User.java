package com.rate_limit.backend.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate()
    {
        this.createdAt = Instant.now();
    }

    UUID getId()
    {
        return this.id;
    }
    void setId(UUID id)
    {
        this.id=id;
    }

    String getPasswordHash()
    {
        return this.passwordHash;
    }
    void setPasswordHash(String passwordHash)
    {
        this.passwordHash=passwordHash;
    }

    String getEmail()
    {
        return this.email;
    }
    void setEmail(String email)
    {
        this.email=email;
    }

    Instant getCreatedAt()
    {
        return this.createdAt;
    }
    void setCreatedAt(Instant createdAt)
    {
        this.createdAt=createdAt;
    }
}
