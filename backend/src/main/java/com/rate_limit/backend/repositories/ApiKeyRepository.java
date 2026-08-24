package com.rate_limit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rate_limit.backend.entities.ApiKey;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey,UUID>{
    
    Optional<ApiKey> findByKeyHash(String keyHash);

    @Query("""
            Select k fron ApiKey k
            JOIN FETCH k.user u
            JOIN FETCH u.plan
            WHERE k.KeyHash = :KeyHash
            """)
    Optional<ApiKey> findByKeyHashWithPlan(@Param("keyHash") String keyHash);
}
