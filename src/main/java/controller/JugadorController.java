package controller;

import dto.InvitacionRequestDTO;
import dto.InvitacionResponseDTO;
import dto.UserResponseDTO;
import dto.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.JugadorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jugadores")
@Tag(name = "Jugadores", description = "Registro y búsqueda de jugadores, envío de invitaciones a equipos (RF-002, RF-003)")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    /**
     * RF-003: Buscar jugadores disponibles (sin equipo).
     */
    @GetMapping("/disponibles")
    @Operation(summary = "Buscar disponibles", description = "Busca jugadores libres mediante filtros opcionales (posicion, nombre, etc.) excluyendo actuales miembros de un equipo si aplica.")
    public ResponseEntity<List<UserResponseDTO>> buscarDisponibles(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String posicion) {
        return ResponseEntity.ok(jugadorService.buscarJugadoresDisponibles(nombre, posicion));
    }

    /**
     * RF-003: Enviar invitación de un capitán a un jugador.
     */
    @PostMapping("/invitar")
    @Operation(summary = "Invitar a equipo", description = "El capitán invita a un jugador disponible a su equipo")
    public ResponseEntity<?> enviarInvitacion(@RequestBody InvitacionRequestDTO request) {
        try {
            InvitacionResponseDTO response = jugadorService.enviarInvitacion(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar la invitación");
        }
    }
}
