package com.rate_limit.backend.entity;

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
    public void onCreate()
    {
        this.createdAt = Instant.now();
    }

    public UUID getId()
    {
        return this.id;
    }
    public void setId(UUID id)
    {
        this.id=id;
    }

    public Plan getPlan()
    {
        return this.plan;
    }

    public void setPlan(Plan plan)
    {
        this.plan = plan;
    }
    
    public String getPasswordHash()
    {
        return this.passwordHash;
    }
    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash=passwordHash;
    }

    public String getEmail()
    {
        return this.email;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }

    public Instant getCreatedAt()
    {
        return this.createdAt;
    }
    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt=createdAt;
    }
}
