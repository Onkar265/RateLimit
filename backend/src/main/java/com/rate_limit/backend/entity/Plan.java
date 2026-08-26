package com.rate_limit.backend.entity;

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

    public UUID getId()
    {
        return this.id;
    }

    public void setId(UUID id)
    {
        this.id=id;
    }

    public String getPlanName()
    {
        return this.planName;
    }

    public void setKeyHash(String planName)
    {
        this.planName=planName;
    }

    public int getDailyLimit()
    {
        return this.dailyLimit;
    }

    public void setDailyLimit(int dailytLimit)
    {
        this.dailyLimit=dailytLimit;
    }

    public int getBurstCapacity()
    {
        return this.burstCapacity;
    }

    public void setBurstCapacity(int burstCapacity)
    {
        this.burstCapacity=burstCapacity;
    }

}
