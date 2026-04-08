package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.MatchMapper;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zeuscodensa.techcupfutbol.core.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Match model = MatchMapper.toEntity(request);
        Match saved = partidoService.registrarPartido(model);
        return ResponseEntity.ok(MatchMapper.toDTO(saved));
    }

    @PutMapping("/{id}/score")
    @Operation(summary = "Update Score", description = "Updates the home and away score of a match")
    public ResponseEntity<MatchResponseDTO> actualizarMarcador(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        log.info("REST request - actualizarMarcador para el match ID: {}", id);
        Match updated = partidoService.actualizarMarcador(id, body.get("homeScore"), body.get("awayScore"));
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @PutMapping("/{id}/lineup")
    @Operation(summary = "Register Lineup", description = "Registers the players who will play in a team for a specific match")
    public ResponseEntity<MatchResponseDTO> registrarAlineacion(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarAlineacion para el match ID: {}", id);
        String teamName = (String) body.get("teamName");
        @SuppressWarnings("unchecked")
        List<String> players = (List<String>) body.get("players");
        Match updated = partidoService.registrarAlineacion(id, teamName, players);
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @PutMapping("/{id}/cards")
    @Operation(summary = "Register Cards", description = "Adds yellow and red cards to the match per player")
    public ResponseEntity<MatchResponseDTO> registrarTarjetas(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarTarjetas para el match ID: {}", id);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> amarillas = (Map<String, List<String>>) body.get("yellowCards");
        @SuppressWarnings("unchecked")
        Map<String, List<String>> rojas = (Map<String, List<String>>) body.get("redCards");
        Match updated = partidoService.registrarTarjetas(id, amarillas, rojas);
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @PutMapping("/{id}/referee")
    @Operation(summary = "Assign Referee", description = "Assigns a referee to a match by email")
    public ResponseEntity<MatchResponseDTO> asignarArbitro(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - asignarArbitro para el match ID: {}", id);
        Match updated = partidoService.asignarArbitro(id, body.get("correoArbitro"));
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @GetMapping("/referee/{refereeEmail}")
    @Operation(summary = "Get My Matches", description = "Returns all matches assigned to a referee")
    public ResponseEntity<List<MatchResponseDTO>> getMisPartidos(@PathVariable String refereeEmail) {
        log.info("REST request - getMisPartidos para el árbitro: {}", refereeEmail);
        return ResponseEntity.ok(partidoService.getMatchesByReferee(refereeEmail).stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping
    @Operation(summary = "Get All Matches", description = "Returns all matches in the system")
    public ResponseEntity<List<MatchResponseDTO>> getAll() {
        log.info("REST request - getAll Partidos");
        return ResponseEntity.ok(partidoService.getAll().stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList()));
    }
}
