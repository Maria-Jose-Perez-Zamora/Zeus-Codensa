package controller;

import dto.InvitacionRequestDTO;
import dto.InvitacionResponseDTO;
import dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.JugadorService;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    /**
     * RF-003: Buscar jugadores disponibles (sin equipo).
     * Query params opcionales: nombre, posicion
     * GET /api/jugadores/disponibles?nombre=Juan&posicion=portero
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<UserResponseDTO>> buscarDisponibles(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String posicion) {
        return ResponseEntity.ok(jugadorService.buscarJugadoresDisponibles(nombre, posicion));
    }

    /**
     * RF-003: Enviar invitación de un capitán a un jugador.
     * POST /api/jugadores/invitar
     */
    @PostMapping("/invitar")
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
