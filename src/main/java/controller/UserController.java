package controller;

import dto.UserRequestDTO;
import dto.UserResponseDTO;
import service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios Generales", description = "Registro de usuarios como Organizador o Administrador (RF-002)")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un usuario como Administrador o Organizador proporcionando base")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Consultar todos", description = "Lista a todos los usuarios del sistema sin filtrar")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}