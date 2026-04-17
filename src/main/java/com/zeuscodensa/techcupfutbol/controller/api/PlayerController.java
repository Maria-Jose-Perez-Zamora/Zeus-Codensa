package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.InvitationAcceptanceDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "Player registration, search, and sending invitations to teams (RF-002, RF-003)")
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/available")
    @Operation(summary = "Find Available Players", description = "Finds free players using optional filters (position, name, etc.)")
    public ResponseEntity<List<UserResponseDTO>> buscarDisponibles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position) {
        log.info("REST request - buscarDisponibles (name: {}, posicion: {})", name, position);
        
        List<User> users = playerService.buscarJugadoresDisponibles(name, position);
        
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
    public ResponseEntity<InvitationResponseDTO> enviarInvitacion(@Valid @RequestBody InvitationRequestDTO request) {
        log.info("REST request - enviarInvitacion para player: {} a team: {}", request.getPlayerEmail(), request.getTeamName());
        
        Invitation invModel = new Invitation();
        invModel.setCaptainEmail(request.getCaptainEmail());
        invModel.setPlayerEmail(request.getPlayerEmail());
        invModel.setTeamName(request.getTeamName());
        
        Invitation savedInv = playerService.enviarInvitacion(invModel);
        
        return ResponseEntity.ok(new InvitationResponseDTO(savedInv));
    }

    @PatchMapping("/invitations/{id}/acceptance")
    @Operation(summary = "Process Invitation", description = "A player accepts or declines an invitation to join a team")
    public ResponseEntity<Void> processInvitation(
            @PathVariable String id,
            @RequestParam String playerEmail,
            @Valid @RequestBody InvitationAcceptanceDTO request) {
        log.info("REST request - processInvitation id: {}, player: {}, status: {}", id, playerEmail, request.getStatus());
        playerService.processInvitation(id, playerEmail, request.getStatus());
        return ResponseEntity.ok().build();
    }
}

