package com.rate_limit.backend.repositories;

import com.rate_limit.backend.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {

    List<UsageLog> findTop50ByApiKey_User_IdOrderByRequestedAtDesc(UUID userId);
}