package com.rate_limit.backend.repositories;

import com.rate_limit.backend.Entities.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {
}