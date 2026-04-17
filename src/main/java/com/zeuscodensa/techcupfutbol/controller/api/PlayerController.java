package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationAcceptanceDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zeuscodensa.techcupfutbol.core.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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
        
        List<User> users = jugadorService.buscarJugadoresDisponibles(name, position);
        
        List<UserResponseDTO> response = users.stream().map(u -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/invitations")
    @Operation(summary = "Invite to Team", description = "The captain invites an available player to their team")
    public ResponseEntity<InvitationResponseDTO> enviarInvitacion(@RequestBody InvitationRequestDTO request) {
        log.info("REST request - enviarInvitacion para player: {} a team: {}", request.getPlayerEmail(), request.getTeamName());
        
        Invitation invModel = new Invitation();
        invModel.setCaptainEmail(request.getCaptainEmail());
        invModel.setPlayerEmail(request.getPlayerEmail());
        invModel.setTeamName(request.getTeamName());
        
        Invitation savedInv = jugadorService.enviarInvitacion(invModel);
        
        return ResponseEntity.ok(new InvitationResponseDTO(savedInv));
    }

    @GetMapping("/invitations")
    @Operation(summary = "Get Player Invitations", description = "Returns all pending invitations and requests for the authenticated player")
    public ResponseEntity<List<InvitationResponseDTO>> getPlayerInvitations(
            org.springframework.security.core.Authentication authentication) {
        String playerEmail = authentication != null ? authentication.getName() : null;
        log.info("REST request - getPlayerInvitations for player: {}", playerEmail);
        
        List<InvitationResponseDTO> requests = jugadorService.getInvitationsByPlayer(playerEmail)
                .stream()
                .map(InvitationResponseDTO::new)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(requests);
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

    @PostMapping("/join-requests")
    @Operation(summary = "Request to Join Team", description = "A player requests to join a team")
    public ResponseEntity<InvitationResponseDTO> solicitarUnirse(
            @RequestParam String teamName,
            org.springframework.security.core.Authentication authentication) {
        
        String playerEmail = authentication != null ? authentication.getName() : null;
        log.info("REST request - solicitarUnirse para player: {} a team: {}", playerEmail, teamName);
        
        Invitation savedInv = jugadorService.enviarSolicitudUnirse(playerEmail, teamName);
        
        return ResponseEntity.ok(new InvitationResponseDTO(savedInv));
    }
}

