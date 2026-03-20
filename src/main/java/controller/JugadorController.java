package controller;

import dto.InvitacionRequestDTO;
import dto.InvitacionResponseDTO;
import dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.JugadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
@Tag(name = "Jugadores", description = "Registro y búsqueda de jugadores, envío de invitaciones a equipos (RF-002, RF-003)")
public class JugadorController {

    private static final Logger log = LoggerFactory.getLogger(JugadorController.class);
    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Buscar disponibles", description = "Busca jugadores libres mediante filtros opcionales (posicion, nombre, etc.)")
    public ResponseEntity<List<UserResponseDTO>> buscarDisponibles(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String posicion) {
        log.info("REST request - buscarDisponibles (nombre: {}, posicion: {})", nombre, posicion);
        return ResponseEntity.ok(jugadorService.buscarJugadoresDisponibles(nombre, posicion));
    }

    @PostMapping("/invitar")
    @Operation(summary = "Invitar a equipo", description = "El capitán invita a un jugador disponible a su equipo")
    public ResponseEntity<InvitacionResponseDTO> enviarInvitacion(@RequestBody InvitacionRequestDTO request) {
        log.info("REST request - enviarInvitacion para jugador: {} a equipo: {}", request.getCorreoJugador(), request.getNombreEquipo());
        return ResponseEntity.ok(jugadorService.enviarInvitacion(request));
    }
}
