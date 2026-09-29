package com.url.shortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping({"/health", "/api/health"})
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "URL Shortener service is running smoothly");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
