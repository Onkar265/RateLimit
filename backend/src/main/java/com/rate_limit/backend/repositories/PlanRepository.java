package com.rate_limit.backend.repositories;

import com.rate_limit.backend.Entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan,UUID> {
    
}
