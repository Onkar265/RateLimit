package com.rate_limit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import com.rate_limit.backend.Entities.User;

public interface UserRepository extends JpaRepository<User,UUID> {
    
    Optional<User>findByEmail(String email);
}
