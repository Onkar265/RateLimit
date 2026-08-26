package com.rate_limit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rate_limit.backend.entity.User;

import java.util.UUID;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,UUID> {
    
    Optional<User>findByEmail(String email);
}
