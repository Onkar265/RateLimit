package com.rate_limit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rate_limit.backend.entity.Plan;

import java.util.Optional;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan,UUID> {
    
    Optional<Plan>getName();
}
