package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.TeamMapper;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import jakarta.validation.Valid;

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
    @Operation(summary = "Get All Teams", description = "Returns the list of all created teams with pagination and optional filtering")
    public ResponseEntity<Page<TeamResponseDTO>> getAllTeams(
            @RequestParam(required = false) String teamName,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("REST request - getAll Teams paginado y filtrado");
        Page<Team> teamsPage = teamService.getAllTeams(teamName, pageable);
        return ResponseEntity.ok(teamsPage.map(TeamMapper::toDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Team by ID", description = "Returns a specific team by its ID")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {
        log.info("REST request - getTeamById: {}", id);
        return ResponseEntity.ok(TeamMapper.toDTO(teamService.getTeamById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Team", description = "Updates an existing team")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequestDTO request) {
        log.info("REST request - updateTeam: {}", id);
        Team updated = teamService.updateTeam(id, TeamMapper.toEntity(request));
        return ResponseEntity.ok(TeamMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Team", description = "Deletes a team by its ID")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.info("REST request - deleteTeam: {}", id);
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
