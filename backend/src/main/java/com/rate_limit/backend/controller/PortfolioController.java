package com.rate_limit.backend.controller;

import com.rate_limit.backend.dto.PortfolioProfile;
import com.rate_limit.backend.dto.PortfolioProject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PortfolioController {

    @GetMapping("/api/portfolio/profile")
    public PortfolioProfile profile() {
        return new PortfolioProfile(
            "Onkar Jha",
            "B.Tech EEE, National Institute of Technology, Warangal (2022–2026) — Backend-focused full-stack developer",
            "I build full-stack systems with a focus on backend architecture — Spring Boot, REST APIs, and real system design like the rate limiter powering this very endpoint. Also active in competitive programming (Codeforces Expert, LeetCode Knight).",
            "https://github.com/Onkar265",
            "https://www.linkedin.com/in/onkar-jha-462157256/"
        );
    }

    @GetMapping("/api/portfolio/projects")
    public List<PortfolioProject> projects() {
        return List.of(
            new PortfolioProject(
                "RateLimit — Rate-Limited API Platform",
                "A rate-limited API platform with atomic Redis-backed token bucket limiting, JWT auth, async usage logging, and a React dashboard. You're using it right now — this exact request just passed through its rate limiter.",
                List.of("Spring Boot", "Redis", "PostgreSQL", "React", "Tailwind CSS", "JWT"),
                "https://github.com/Onkar265/RateLimit"
            ),
            new PortfolioProject(
                "Online Gambling Platform (Full Stack)",
                "A full-stack betting platform with wallet and transaction management, secure REST APIs for authentication and bet placement, and role-based access control via JWT.",
                List.of("Spring Boot", "React.js", "MySQL", "JWT Authentication", "Tailwind CSS", "JPA/Hibernate"),
                null
            ),
            new PortfolioProject(
                "Food Delivery Website",
                "A responsive food ordering platform with restaurant listings, menu browsing, cart management, and dynamic routing, including unit and integration tests.",
                List.of("React.js", "Redux Toolkit", "Tailwind CSS", "React Router", "Jest"),
                null
            )
        );
    }

    @GetMapping("/api/portfolio/skills")
    public List<String> skills() {
        return List.of(
            "Java", "Spring Boot", "React.js", "JavaScript", "Python", "C++",
            "PostgreSQL", "MySQL", "Redis", "Docker", "JWT Authentication",
            "Tailwind CSS", "Git & GitHub", "REST API Design"
        );
    }
}