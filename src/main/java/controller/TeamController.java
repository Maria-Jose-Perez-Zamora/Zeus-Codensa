package controller;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import core.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Formation of teams for the sports tournament (RF-003)")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create Team", description = "Registers a team associating colors and crest with a name")
    public ResponseEntity<TeamResponseDTO> createTeam(@RequestBody TeamRequestDTO request) {
        log.info("REST request - createTeam: {}", request.getTeamName());
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @GetMapping("/all")
    @Operation(summary = "List Teams", description = "Returns all registered teams generally")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        log.info("REST request - getAll Teams");
        return ResponseEntity.ok(teamService.getAllTeams());
    }
}
