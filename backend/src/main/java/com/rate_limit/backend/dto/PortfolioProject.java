package com.rate_limit.backend.dto;

import java.util.List;

public record PortfolioProject(String name, String description, List<String> techStack, String url) {
}