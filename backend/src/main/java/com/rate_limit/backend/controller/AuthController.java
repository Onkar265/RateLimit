package com.rate_limit.backend.controller;

import com.rate_limit.backend.dto.AuthResponse;
import com.rate_limit.backend.dto.LoginRequest;
import com.rate_limit.backend.dto.RegisterRequest;
import com.rate_limit.backend.entity.Plan;
import com.rate_limit.backend.entity.User;
import com.rate_limit.backend.repositories.PlanRepository;
import com.rate_limit.backend.repositories.UserRepository;
import com.rate_limit.backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                           PlanRepository planRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(409).build();
        }

        Plan freePlan = planRepository.findAll().stream()
            .filter(p -> p.getName().equals("free"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("free plan not seeded"));

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPlan(freePlan);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.status(201).body(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtService.generateToken(request.email());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}