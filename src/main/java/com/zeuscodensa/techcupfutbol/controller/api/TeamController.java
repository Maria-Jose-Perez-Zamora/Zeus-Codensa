package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.InvitationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.TeamMapper;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.service.PlayerService;
import com.zeuscodensa.techcupfutbol.core.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Formation of teams for the sports tournament (RF-003)")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;
    private final PlayerService playerService;

    public TeamController(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @PostMapping
    @Operation(summary = "Create Team", description = "Creates a new team. The authenticated captain's email is automatically stored.")
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestBody TeamRequestDTO request,
            Authentication authentication) {
        log.info("REST request - createTeam: {}", request.getTeamName());
        String captainEmail = authentication != null ? authentication.getName() : null;
        Team newTeam = TeamMapper.toEntity(request);
        newTeam.setCaptainEmail(captainEmail);
        Team saved = teamService.createTeam(newTeam, request.getPlayerEmails());
        return ResponseEntity.ok(TeamMapper.toDTO(saved));
    }

    @GetMapping
    @Operation(summary = "Get All Teams", description = "Returns the list of all created teams")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        log.info("REST request - getAll Teams");
        return ResponseEntity.ok(teamService.getAllTeams().stream()
                .map(TeamMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/my-team")
    @Operation(summary = "Get My Team", description = "Returns the team where the authenticated user is captain or a player")
    public ResponseEntity<TeamResponseDTO> getMyTeam(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        log.info("REST request - getMyTeam para usuario: {}", email);
        Team myTeam = teamService.getAllTeams().stream()
                .filter(t -> (t.getCaptainEmail() != null && t.getCaptainEmail().equals(email)) ||
                             (t.getPlayers() != null && t.getPlayers().stream().anyMatch(p -> p.getEmail().equals(email))))
                .findFirst()
                .orElse(null);
        
        if (myTeam == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TeamResponseDTO(myTeam));
    }

    // ── Join-request endpoints (captain-side) ──────────────────────────────

    @GetMapping("/join-requests")
    @Operation(summary = "Get Join Requests", description = "Returns all pending join requests for the authenticated captain's team")
    public ResponseEntity<List<InvitationResponseDTO>> getJoinRequests(Authentication authentication) {
        String captainEmail = authentication != null ? authentication.getName() : null;
        log.info("REST request - getJoinRequests for captain: {}", captainEmail);
        List<InvitationResponseDTO> requests = playerService
                .getInvitationsByCaptain(captainEmail)
                .stream()
                .map(InvitationResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/join-requests/{id}")
    @Operation(summary = "Process Join Request", description = "Captain accepts or declines a player's join request. status = ACEPTADA | DECLINADA")
    public ResponseEntity<Void> processJoinRequest(
            @PathVariable String id,
            @RequestParam String status,
            Authentication authentication) {
        String captainEmail = authentication != null ? authentication.getName() : null;
        log.info("REST request - processJoinRequest id: {}, status: {}, captain: {}", id, status, captainEmail);
        playerService.captainProcessJoinRequest(id, captainEmail, status);
        return ResponseEntity.ok().build();
    }
}
