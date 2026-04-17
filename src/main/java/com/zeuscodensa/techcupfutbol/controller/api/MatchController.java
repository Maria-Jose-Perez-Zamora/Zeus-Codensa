package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchCardsRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchLineupRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchScoreRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RefereeAssignmentRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.MatchMapper;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Matches", description = "Management of matches, results, lineups, cards, and referees (RF-006, RF-008)")
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    @Operation(summary = "Create Match", description = "Creates a new match in the tournament")
    public ResponseEntity<MatchResponseDTO> registrarPartido(@Valid @RequestBody MatchRequestDTO request) {
        log.info("REST request - registrarPartido");
        Match model = MatchMapper.toEntity(request);
        Match saved = matchService.registrarPartido(model);
        return ResponseEntity.ok(MatchMapper.toDTO(saved));
    }

    @PutMapping("/{id}/score")
    @Operation(summary = "Update Score", description = "Updates the home and away score of a match")
    public ResponseEntity<MatchResponseDTO> actualizarMarcador(@PathVariable String id, @Valid @RequestBody MatchScoreRequestDTO body) {
        log.info("REST request - actualizarMarcador para el match ID: {}", id);
        Match updated = matchService.actualizarMarcador(id, body.getHomeScore(), body.getAwayScore());
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @PutMapping("/{id}/lineup")
    @Operation(summary = "Register Lineup", description = "Registers the players who will play in a team for a specific match")
    public ResponseEntity<MatchResponseDTO> registrarAlineacion(@PathVariable String id, @Valid @RequestBody MatchLineupRequestDTO body) {
        log.info("REST request - registrarAlineacion para el match ID: {}", id);
        Match updated = matchService.registrarAlineacion(id, body.getTeamName(), body.getPlayers());
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @PutMapping("/{id}/cards")
    @Operation(summary = "Register Cards", description = "Adds yellow and red cards to the match per player")
    public ResponseEntity<MatchResponseDTO> registrarTarjetas(@PathVariable String id, @RequestBody MatchCardsRequestDTO body) {
        log.info("REST request - registrarTarjetas para el match ID: {}", id);
        Match updated = matchService.registrarTarjetas(id, body.getYellowCards(), body.getRedCards());
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @PutMapping("/{id}/referee")
    @Operation(summary = "Assign Referee", description = "Assigns a referee to a match by email")
    public ResponseEntity<MatchResponseDTO> asignarArbitro(@PathVariable String id, @Valid @RequestBody RefereeAssignmentRequestDTO body) {
        log.info("REST request - asignarArbitro para el match ID: {}", id);
        Match updated = matchService.asignarArbitro(id, body.getRefereeEmail());
        return ResponseEntity.ok(MatchMapper.toDTO(updated));
    }

    @GetMapping("/referee/{refereeEmail}")
    @Operation(summary = "Get My Matches", description = "Returns all matches assigned to a referee")
    public ResponseEntity<List<MatchResponseDTO>> getMisPartidos(@PathVariable String refereeEmail) {
        log.info("REST request - getMisPartidos para el árbitro: {}", refereeEmail);
        return ResponseEntity.ok(matchService.getMatchesByReferee(refereeEmail).stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping
    @Operation(summary = "Get All Matches", description = "Returns all matches in the system")
    public ResponseEntity<List<MatchResponseDTO>> getAll() {
        log.info("REST request - getAll Partidos");
        return ResponseEntity.ok(matchService.getAll().stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList()));
    }
}
