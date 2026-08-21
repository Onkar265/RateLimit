package com.rate_limit.backend.Entities;

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

    @Column(name = "burst-capacity", nullable = false)
    private int burstCapacity;
}
