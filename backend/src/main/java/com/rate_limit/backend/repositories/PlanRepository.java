package com.rate_limit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rate_limit.backend.entities.Plan;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan,UUID> {
    
}
