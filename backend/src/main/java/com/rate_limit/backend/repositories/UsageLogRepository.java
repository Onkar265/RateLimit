package com.rate_limit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rate_limit.backend.entities.UsageLog;

import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {
}