package com.rate_limit.backend.controller;

import com.rate_limit.backend.dto.ApiKeySummary;
import com.rate_limit.backend.dto.CreateApiKeyRequest;
import com.rate_limit.backend.dto.CreateApiKeyResponse;
import com.rate_limit.backend.security.UserPrincipal;
import com.rate_limit.backend.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<CreateApiKeyResponse> createKey(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateApiKeyRequest request) {
        CreateApiKeyResponse response = apiKeyService.createKey(principal.getUserId(), request.label());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public List<ApiKeySummary> listKeys(@AuthenticationPrincipal UserPrincipal principal) {
        return apiKeyService.listKeys(principal.getUserId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeKey(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {
        apiKeyService.revokeKey(principal.getUserId(), id);
        return ResponseEntity.noContent().build();
    }
}