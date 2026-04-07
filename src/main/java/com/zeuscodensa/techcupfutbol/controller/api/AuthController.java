package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.LoginRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.LoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zeuscodensa.techcupfutbol.core.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for login and user validation")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Login", description = "Produces a simple JWT Base64 Token linked to the Role")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("REST request - intento de login de usuario: {}", request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }
}
