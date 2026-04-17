package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.TeamMapper;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create Team", description = "Creates a new team and associates existing users via email. Solo para ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta o error de validación"),
        @ApiResponse(responseCode = "401", description = "No autorizado (Token faltante o inválido)"),
        @ApiResponse(responseCode = "403", description = "Prohibido (Rol incorrecto, se requiere ADMIN)"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TeamResponseDTO> createTeam(
            @Parameter(description = "Datos del equipo a crear") @Valid @RequestBody TeamRequestDTO request) {
        log.info("REST request - createTeam: {}", request.getTeamName());
        Team newTeam = TeamMapper.toEntity(request);
        Team saved = teamService.createTeam(newTeam, request.getPlayerEmails());
        return ResponseEntity.ok(TeamMapper.toDTO(saved));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Get All Teams", description = "Returns the list of all created teams con paginación y filtros. Para ADMIN y USER.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido (Rol incorrecto)"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Page<TeamResponseDTO>> getAllTeams(
            @Parameter(description = "Filtro opcional por nombre de equipo") @RequestParam(required = false) String teamName,
            @Parameter(description = "Configuración de paginación") @PageableDefault(size = 10) Pageable pageable) {
        log.info("REST request - getAll Teams paginado y filtrado");
        Page<Team> teamsPage = teamService.getAllTeams(teamName, pageable);
        return ResponseEntity.ok(teamsPage.map(TeamMapper::toDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Get Team by ID", description = "Returns a specific team by its ID. Para ADMIN y USER.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido (Rol incorrecto)"),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TeamResponseDTO> getTeamById(
            @Parameter(description = "ID del equipo") @PathVariable Long id) {
        log.info("REST request - getTeamById: {}", id);
        return ResponseEntity.ok(TeamMapper.toDTO(teamService.getTeamById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update Team", description = "Updates an existing team. Solo para ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido (Rol incorrecto, se requiere ADMIN)"),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @Parameter(description = "ID del equipo a actualizar") @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del equipo") @Valid @RequestBody TeamRequestDTO request) {
        log.info("REST request - updateTeam: {}", id);
        Team updated = teamService.updateTeam(id, TeamMapper.toEntity(request));
        return ResponseEntity.ok(TeamMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete Team", description = "Deletes a team by its ID. Solo para ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido (Rol incorrecto, se requiere ADMIN)"),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteTeam(
            @Parameter(description = "ID del equipo a eliminar") @PathVariable Long id) {
        log.info("REST request - deleteTeam: {}", id);
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
