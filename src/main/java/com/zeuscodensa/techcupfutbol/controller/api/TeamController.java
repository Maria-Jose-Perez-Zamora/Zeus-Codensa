package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.TeamMapper;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Formation of teams for the sports tournament (RF-003)")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @Operation(summary = "Create Team", description = "Creates a new team and associates existing users via email")
    public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO request) {
        log.info("REST request - createTeam: {}", request.getTeamName());
        Team newTeam = TeamMapper.toEntity(request);
        Team saved = teamService.createTeam(newTeam, request.getPlayerEmails());
        return ResponseEntity.ok(TeamMapper.toDTO(saved));
    }

    @GetMapping
    @Operation(summary = "Get All Teams", description = "Returns the list of all created teams")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        log.info("REST request - getAll Teams");
        return ResponseEntity.ok(teamService.getAllTeams().stream().map(TeamMapper::toDTO).collect(Collectors.toList()));
    }
}
