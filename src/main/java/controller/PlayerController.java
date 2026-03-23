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
@Tag(name = "Players", description = "Player registration, search, and sending invitations to teams (RF-002, RF-003)")
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService jugadorService;

    public PlayerController(PlayerService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping("/available")
    @Operation(summary = "Find Available Players", description = "Finds free players using optional filters (position, name, etc.)")
    public ResponseEntity<List<UserResponseDTO>> buscarDisponibles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position) {
        log.info("REST request - buscarDisponibles (name: {}, posicion: {})", name, position);
        return ResponseEntity.ok(jugadorService.buscarJugadoresDisponibles(name, position));
    }

    @PostMapping("/invite")
    @Operation(summary = "Invite to Team", description = "The captain invites an available player to their team")
    public ResponseEntity<InvitationResponseDTO> enviarInvitacion(@RequestBody InvitationRequestDTO request) {
        log.info("REST request - enviarInvitacion para player: {} a team: {}", request.getPlayerEmail(), request.getTeamName());
        return ResponseEntity.ok(jugadorService.enviarInvitacion(request));
    }

    @PutMapping("/invitations/{id}/accept")
    @Operation(summary = "Accept Invitation", description = "A player accepts an invitation to join a team")
    public ResponseEntity<Void> acceptInvitation(@PathVariable String id, @RequestParam String playerEmail) {
        log.info("REST request - acceptInvitation id: {} player: {}", id, playerEmail);
        jugadorService.acceptInvitation(id, playerEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/invitations/{id}/decline")
    @Operation(summary = "Decline Invitation", description = "A player declines an invitation to join a team")
    public ResponseEntity<Void> declineInvitation(@PathVariable String id, @RequestParam String playerEmail) {
        log.info("REST request - declineInvitation id: {} player: {}", id, playerEmail);
        jugadorService.declineInvitation(id, playerEmail);
        return ResponseEntity.ok().build();
    }
}
