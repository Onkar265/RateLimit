package com.rate_limit.backend.repositories;

import com.rate_limit.backend.dto.UsageLogEntry;
import com.rate_limit.backend.entity.UsageLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {

    @Query("""
        SELECT new com.rate_limit.backend.dto.UsageLogEntry(
            u.apiKey.label, u.endpoint, u.method, u.statusCode, u.responseTimeMs, u.requestedAt
        )
        FROM UsageLog u
        WHERE u.apiKey.user.id = :userId
        ORDER BY u.requestedAt DESC
        """)
    List<UsageLogEntry> findRecentUsageByUserId(@Param("userId") UUID userId, Pageable pageable);
}