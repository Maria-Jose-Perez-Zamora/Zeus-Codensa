package controller;

import dependencies.dto.MatchRequestDTO;
import dependencies.dto.MatchResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Matches", description = "Management of matches, results, lineups, cards, and referees (RF-006, RF-008)")
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);
    private final MatchService partidoService;

    public MatchController(MatchService partidoService) {
        this.partidoService = partidoService;
    }

    @PostMapping
    @Operation(summary = "Create Match", description = "Creates a new match in the tournament")
    public ResponseEntity<MatchResponseDTO> registrarPartido(@RequestBody MatchRequestDTO request) {
        log.info("REST request - registrarPartido");
        return ResponseEntity.ok(partidoService.registrarPartido(request));
    }

    @PutMapping("/{id}/score")
    @Operation(summary = "Update Score", description = "Updates the home and away score of a match")
    public ResponseEntity<MatchResponseDTO> actualizarMarcador(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        log.info("REST request - actualizarMarcador para el match ID: {}", id);
        return ResponseEntity.ok(partidoService.actualizarMarcador(id, body.get("homeScore"), body.get("awayScore")));
    }

    @PutMapping("/{id}/lineup")
    @Operation(summary = "Register Lineup", description = "Registers the players who will play in a team for a specific match")
    public ResponseEntity<MatchResponseDTO> registrarAlineacion(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarAlineacion para el match ID: {}", id);
        String teamName = (String) body.get("teamName");
        @SuppressWarnings("unchecked")
        List<String> players = (List<String>) body.get("players");
        return ResponseEntity.ok(partidoService.registrarAlineacion(id, teamName, players));
    }

    @PutMapping("/{id}/cards")
    @Operation(summary = "Register Cards", description = "Adds yellow and red cards to the match per player")
    public ResponseEntity<MatchResponseDTO> registrarTarjetas(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarTarjetas para el match ID: {}", id);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> amarillas = (Map<String, List<String>>) body.get("yellowCards");
        @SuppressWarnings("unchecked")
        Map<String, List<String>> rojas = (Map<String, List<String>>) body.get("redCards");
        return ResponseEntity.ok(partidoService.registrarTarjetas(id, amarillas, rojas));
    }

    @PutMapping("/{id}/referee")
    @Operation(summary = "Assign Referee", description = "Assigns a referee to a match by email")
    public ResponseEntity<MatchResponseDTO> asignarArbitro(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - asignarArbitro para el match ID: {}", id);
        return ResponseEntity.ok(partidoService.asignarArbitro(id, body.get("correoArbitro")));
    }

    @GetMapping("/referee/{refereeEmail}")
    @Operation(summary = "Get My Matches", description = "Returns all matches assigned to a referee")
    public ResponseEntity<List<MatchResponseDTO>> getMisPartidos(@PathVariable String refereeEmail) {
        log.info("REST request - getMisPartidos para el árbitro: {}", refereeEmail);
        return ResponseEntity.ok(partidoService.getMatchesByReferee(refereeEmail));
    }

    @GetMapping
    @Operation(summary = "Get All Matches", description = "Returns all matches in the system")
    public ResponseEntity<List<MatchResponseDTO>> getAll() {
        log.info("REST request - getAll Partidos");
        return ResponseEntity.ok(partidoService.getAll());
    }
}
