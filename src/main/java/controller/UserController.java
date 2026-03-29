package controller;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "General Users", description = "User registration for Organizers or Administrators (RF-002)")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Register User", description = "Registers a new user (Player, Organizer, Referee, Captain, or Admin) in the system")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) {
        log.info("REST request - register user: {}", request.getEmail());
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @GetMapping
    @Operation(summary = "Get All Users", description = "Returns a list of all registered users")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        log.info("REST request - getAll Usuarios");
        return ResponseEntity.ok(userService.getAllUsers());
    }
}