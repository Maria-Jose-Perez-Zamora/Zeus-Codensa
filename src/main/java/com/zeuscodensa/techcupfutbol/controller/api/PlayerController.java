package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationAcceptanceDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zeuscodensa.techcupfutbol.core.service.PlayerService;
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

    @PostMapping("/invitations")
    @Operation(summary = "Invite to Team", description = "The captain invites an available player to their team")
    public ResponseEntity<InvitationResponseDTO> enviarInvitacion(@RequestBody InvitationRequestDTO request) {
        log.info("REST request - enviarInvitacion para player: {} a team: {}", request.getPlayerEmail(), request.getTeamName());
        return ResponseEntity.ok(jugadorService.enviarInvitacion(request));
    }

    @PatchMapping("/invitations/{id}/acceptance")
    @Operation(summary = "Process Invitation", description = "A player accepts or declines an invitation to join a team")
    public ResponseEntity<Void> processInvitation(
            @PathVariable String id,
            @RequestParam String playerEmail,
            @RequestBody InvitationAcceptanceDTO request) {
        log.info("REST request - processInvitation id: {}, player: {}, status: {}", id, playerEmail, request.getStatus());
        jugadorService.processInvitation(id, playerEmail, request.getStatus());
        return ResponseEntity.ok().build();
    }
}
