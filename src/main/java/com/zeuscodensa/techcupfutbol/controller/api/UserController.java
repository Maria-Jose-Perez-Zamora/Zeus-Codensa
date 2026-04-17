package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.UserMapper;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "General Users", description = "User registration for Organizers or Administrators (RF-002)")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Returns the profile of the authenticated user")
    public ResponseEntity<UserResponseDTO> getProfile(Principal principal) {
        log.info("REST request - get profile for user: {}", principal.getName());
        
        User userModel = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        return ResponseEntity.ok(UserMapper.toDTO(userModel));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Updates the profile of the authenticated user")
    public ResponseEntity<UserResponseDTO> updateProfile(Principal principal, @Valid @RequestBody UserRequestDTO request) {
        log.info("REST request - update profile for user: {}", principal.getName());
        
        User updateData = UserMapper.toEntity(request);
        User updatedUser = userService.updateUser(principal.getName(), updateData);
        
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }
    @GetMapping
    @Operation(summary = "Get All Users", description = "Returns a list of all registered users")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        log.info("REST request - getAll Usuarios");
        
        List<UserResponseDTO> response = userService.getAllUsers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(response);
    }
}