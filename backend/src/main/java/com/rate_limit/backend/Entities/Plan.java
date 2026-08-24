package com.rate_limit.backend.entities;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Plans")
public class Plan {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "daily_limit", nullable = false)
    private int dailyLimit;

    @Column(name = "burst_capacity", nullable = false)
    private int burstCapacity;

    UUID getId()
    {
        return this.id;
    }

    void setId(UUID id)
    {
        this.id=id;
    }

    String getPlanName()
    {
        return this.planName;
    }

    void setKeyHash(String planName)
    {
        this.planName=planName;
    }

    int getDailyLimit()
    {
        return this.dailyLimit;
    }

    void setDailyLimit(int dailytLimit)
    {
        this.dailyLimit=dailytLimit;
    }

    int getBurstCapacity()
    {
        return this.burstCapacity;
    }

    void setBurstCapacity(int burstCapacity)
    {
        this.burstCapacity=burstCapacity;
    }

}
