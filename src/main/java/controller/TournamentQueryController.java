package controller;

import core.model.Standing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.StatisticsService;
import core.service.BracketService;
import core.service.StandingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments/query")
@Tag(name = "Tournament Information and Queries", description = "Visualization of Standings, Statistics, Brackets, and Calendars (RF-007, RF-008)")
public class TournamentQueryController {

    private static final Logger log = LoggerFactory.getLogger(TournamentQueryController.class);

    private final StandingService tablaService;
    private final BracketService llaveService;
    private final StatisticsService estadisticasService;
    private final dependencies.persistence.repository.MatchRepository matchRepository;

    public TournamentQueryController(StandingService tablaService, BracketService llaveService, StatisticsService estadisticasService, dependencies.persistence.repository.MatchRepository matchRepository) {
        this.tablaService = tablaService;
        this.llaveService = llaveService;
        this.estadisticasService = estadisticasService;
        this.matchRepository = matchRepository;
    }

    @GetMapping("")
    @Operation(summary = "Query Index", description = "Base endpoint to validate connection")
    public ResponseEntity<Map<String, String>> index() {
        return ResponseEntity.ok(Map.of(
            "status", "Query API is running",
            "message", "Use /{tournament}/standings, /{tournament}/brackets/{phase}, /{tournament}/calendar, /{tournament}/statistics, /{tournament}/scorers"
        ));
    }

    @GetMapping("/{tournament}/standings")
    @Operation(summary = "Standings Table", description = "Generates the general standing table from the active tournament summing wins and draws")
    public ResponseEntity<List<Standing>> getTabla(@PathVariable String tournament) {
        log.info("REST request - getTabla de posiciones para tournament: {}", tournament);
        return ResponseEntity.ok(tablaService.calcularTabla(tournament));
    }

    @GetMapping("/{tournament}/brackets/{phase}")
    @Operation(summary = "Generate Knockout Brackets", description = "Draws quarterfinals, semifinals, or finals")
    public ResponseEntity<?> getBrackets(@PathVariable String tournament, @PathVariable String phase) {
        log.info("REST request - getBrackets eliminatorias para tournament: {}, phase: {}", tournament, phase);
        return ResponseEntity.ok(llaveService.generarLlaves(tournament, phase));
    }

    @GetMapping("/{tournament}/calendar")
    @Operation(summary = "Match Calendar", description = "Returns scheduled matches")
    public ResponseEntity<?> getCalendar(@PathVariable String tournament) {
        log.info("REST request - getCalendar para tournament: {}", tournament);
        List<Map<String, Object>> calendario = matchRepository.findByTournamentName(tournament).stream()
                .map(dependencies.persistence.mapper.EntityToModelMapper::toMatchModel)
                .filter(p -> "SCHEDULED".equals(p.getStatus()))
                .map(p -> Map.<String, Object>of(
                        "id", p.getId(),
                        "homeTeam", p.getHomeTeam(),
                        "awayTeam", p.getAwayTeam(),
                        "matchDate", p.getMatchDate(),
                        "status", p.getStatus()
                ))
                .collect(Collectors.toList());

        if (calendario.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No scheduled matches for this tournament"));
        }
        return ResponseEntity.ok(calendario);
    }

    @GetMapping("/{tournament}/results")
    @Operation(summary = "Historical Results", description = "All finished matches")
    public ResponseEntity<?> getResultados(@PathVariable String tournament) {
        log.info("REST request - getResultados Historicos para tournament: {}", tournament);
        List<Map<String, Object>> resultados = matchRepository.findByTournamentName(tournament).stream()
                .map(dependencies.persistence.mapper.EntityToModelMapper::toMatchModel)
                .filter(p -> "FINISHED".equals(p.getStatus()))
                .map(p -> Map.<String, Object>of(
                        "id", p.getId(),
                        "homeTeam", p.getHomeTeam(),
                        "awayTeam", p.getAwayTeam(),
                        "homeScore", p.getHomeScore(),
                        "awayScore", p.getAwayScore(),
                        "matchDate", p.getMatchDate()
                ))
                .collect(Collectors.toList());

        if (resultados.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No results available for this tournament"));
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{tournament}/statistics")
    @Operation(summary = "Global Statistics", description = "Summary of goals for/against per team")
    public ResponseEntity<?> getEstadisticas(@PathVariable String tournament) {
        log.info("REST request - getEstadisticas para tournament: {}", tournament);
        List<Standing> standingTable = tablaService.calcularTabla(tournament);
        if (standingTable.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No statistics available for this tournament"));
        }
        return ResponseEntity.ok(standingTable);
    }

    @GetMapping("/{tournament}/scorers")
    @Operation(summary = "Top Scorers Table", description = "All top scorers aggregated across matches")
    public ResponseEntity<?> getScorers(@PathVariable String tournament) {
        log.info("REST request - getScorers para tournament: {}", tournament);
        List<Map<String, Object>> goleadores = estadisticasService.getTopScorers(tournament);
        if (goleadores.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No scorers registered for this tournament"));
        }
        return ResponseEntity.ok(goleadores);
    }

    @GetMapping("/{tournament}/history/{team}")
    @Operation(summary = "Club History", description = "Chronological performance of a given team")
    public ResponseEntity<?> getTeamHistory(@PathVariable String tournament, @PathVariable String team) {
        log.info("REST request - getTeamHistory para tournament: {}, team: {}", tournament, team);
        List<Map<String, Object>> historial = estadisticasService.getTeamHistory(tournament, team);
        if (historial.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No history available for this team in the tournament"));
        }
        return ResponseEntity.ok(historial);
    }
}
