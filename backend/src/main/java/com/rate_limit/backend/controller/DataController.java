package com.rate_limit.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Map;

@RestController
public class DataController {

    @GetMapping("/api/data/ping")
    public Map<String, Object> ping() {
        return Map.of(
            "status", "ok",
            "timestamp", Instant.now().toString()
        );
    }
}