package com.zeuscodensa.techcupfutbol.controller.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth/google")
@Tag(name = "External Authentication", description = "OAuth2 Google authentication endpoints for external users")
public class ExternalAuthController {

    @GetMapping("/start")
    @Operation(summary = "Start Google OAuth2", description = "Returns the URL to start Google OAuth2 login")
    public ResponseEntity<Map<String, String>> startGoogleOAuth2Flow() {
        return ResponseEntity.ok(Map.of("authorizationUrl", "/oauth2/authorization/google"));
    }
}
