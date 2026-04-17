package com.zeuscodensa.techcupfutbol.controller.api;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.UserMapper;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        log.info("REST request - get profile");
        
        User userModel = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        return ResponseEntity.ok(UserMapper.toDTO(userModel));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Updates the profile of the authenticated user")
    public ResponseEntity<UserResponseDTO> updateProfile(Principal principal, @Valid @RequestBody UserRequestDTO request) {
        log.info("REST request - update profile");
        
        User updateData = UserMapper.toEntity(request);
        User updatedUser = userService.updateUser(principal.getName(), updateData);
        
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR_SISTEMA')")
    @Operation(summary = "Get All Users Paginated", description = "Returns a paginated list of all registered users (Admin only)")
    public ResponseEntity<Page<UserResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("REST request - getAll Usuarios paginated");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDTO> response = userService.getAllUsers(pageable)
                .map(UserMapper::toDTO);
                
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR_SISTEMA')")
    @Operation(summary = "Get User by Email", description = "Returns user details by email (Admin only)")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        log.info("REST request - get user by email");
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PostMapping("/register")
    @Operation(summary = "Create User (Public)", description = "Creates a new user in the system (Public registration)")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        log.info("REST request - create user");
        User userModel = UserMapper.toEntity(request);
        User savedUser = userService.registerUser(userModel);
        return ResponseEntity.ok(UserMapper.toDTO(savedUser));
    }

    @PutMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR_SISTEMA')")
    @Operation(summary = "Update User by Email", description = "Updates a user's details by email (Admin only)")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String email, 
            @Valid @RequestBody UserRequestDTO request) {
        log.info("REST request - update user by email");
        User updateData = UserMapper.toEntity(request);
        User updatedUser = userService.updateUser(email, updateData);
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR_SISTEMA')")
    @Operation(summary = "Delete User", description = "Deletes a user by email (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        log.info("REST request - delete user by email");
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}