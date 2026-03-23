package controller;

import dependencies.dto.InvitationRequestDTO;
import dependencies.dto.InvitationResponseDTO;
import dependencies.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Jugadores", description = "Registro y búsqueda de players, envío de invitations a teams (RF-002, RF-003)")
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService jugadorService;

    public PlayerController(PlayerService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Buscar disponibles", description = "Busca players libres mediante filtros opcionales (posicion, name, etc.)")
    public ResponseEntity<List<UserResponseDTO>> buscarDisponibles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String posicion) {
        log.info("REST request - buscarDisponibles (name: {}, posicion: {})", name, posicion);
        return ResponseEntity.ok(jugadorService.buscarJugadoresDisponibles(name, posicion));
    }

    @PostMapping("/invitar")
    @Operation(summary = "Invitar a team", description = "El capitán invita a un player disponible a su team")
    public ResponseEntity<InvitationResponseDTO> enviarInvitacion(@RequestBody InvitationRequestDTO request) {
        log.info("REST request - enviarInvitacion para player: {} a team: {}", request.getCorreoJugador(), request.getNombreEquipo());
        return ResponseEntity.ok(jugadorService.enviarInvitacion(request));
    }
}
