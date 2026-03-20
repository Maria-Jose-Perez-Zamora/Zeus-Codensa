package controller;

import dto.TeamRequestDTO;
import dto.TeamResponseDTO;
import service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Equipos", description = "Conformación de equipos del torneo deportivo (RF-003)")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear equipo", description = "Registra un equipo asociando colores al escudo y nombre")
    public ResponseEntity<?> createTeam(@RequestBody TeamRequestDTO request) {
        try {
            TeamResponseDTO response = teamService.createTeam(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Listar equipos", description = "Retorna de manera general todos los equipos inscritos en memoria")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }
}
