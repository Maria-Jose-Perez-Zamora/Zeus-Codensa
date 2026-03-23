package controller;

import core.model.Standing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.StatisticsService;
import core.service.BracketService;
import core.service.StandingService;
import dependencies.util.DataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments/consulta")
@Tag(name = "Consulta e Información del Tournament", description = "Visualización de Tablas, Estadísticas, Llaves eliminatorias y Calendarios (RF-007, RF-008)")
public class TournamentQueryController {

    private static final Logger log = LoggerFactory.getLogger(TournamentQueryController.class);

    private final StandingService tablaService;
    private final BracketService llaveService;
    private final StatisticsService estadisticasService;

    public TournamentQueryController(StandingService tablaService, BracketService llaveService, StatisticsService estadisticasService) {
        this.tablaService = tablaService;
        this.llaveService = llaveService;
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("")
    @Operation(summary = "Indice de Consultas", description = "Endpoint base que valida conexion")
    public ResponseEntity<Map<String, String>> index() {
        return ResponseEntity.ok(Map.of(
            "status", "Consulta API is running",
            "message", "Usa /{tournament}/standingTable, /{tournament}/llaves/{phase}, /{tournament}/calendario, /{tournament}/estadisticas, /{tournament}/goleadores"
        ));
    }

    @GetMapping("/{tournament}/standingTable")
    @Operation(summary = "Tabla de posiciones", description = "Genera la standingTable general a partir del tournament activo sumando victorias y empates")
    public ResponseEntity<List<Standing>> getTabla(@PathVariable String tournament) {
        log.info("REST request - getTabla de posiciones para tournament: {}", tournament);
        return ResponseEntity.ok(tablaService.calcularTabla(tournament));
    }

    @GetMapping("/{tournament}/llaves/{phase}")
    @Operation(summary = "Generación Llaves Eliminatorias", description = "Dibuja cuartos, semifinal o final")
    public ResponseEntity<?> getLlaves(@PathVariable String tournament, @PathVariable String phase) {
        log.info("REST request - getLlaves eliminatorias para tournament: {}, phase: {}", tournament, phase);
        return ResponseEntity.ok(llaveService.generarLlaves(tournament, phase));
    }

    @GetMapping("/{tournament}/calendario")
    @Operation(summary = "Calendario Partidos", description = "Devuelve los matches programados")
    public ResponseEntity<?> getCalendario(@PathVariable String tournament) {
        log.info("REST request - getCalendario para tournament: {}", tournament);
        List<Map<String, Object>> calendario = DataStorage.matches.stream()
                .filter(p -> p.getTournamentName().equals(tournament) && "SCHEDULED".equals(p.getStatus()))
                .map(p -> Map.<String, Object>of(
                        "id", p.getId(),
                        "homeTeam", p.getHomeTeam(),
                        "awayTeam", p.getAwayTeam(),
                        "matchDate", p.getMatchDate(),
                        "status", p.getStatus()
                ))
                .collect(Collectors.toList());

        if (calendario.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay matches programados para este tournament"));
        }
        return ResponseEntity.ok(calendario);
    }

    @GetMapping("/{tournament}/resultados")
    @Operation(summary = "Resultados Historicos", description = "Todos los matches ya finalizados")
    public ResponseEntity<?> getResultados(@PathVariable String tournament) {
        log.info("REST request - getResultados Historicos para tournament: {}", tournament);
        List<Map<String, Object>> resultados = DataStorage.matches.stream()
                .filter(p -> p.getTournamentName().equals(tournament) && "FINISHED".equals(p.getStatus()))
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
            return ResponseEntity.ok(Map.of("mensaje", "No hay resultados disponibles para este tournament"));
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{tournament}/estadisticas")
    @Operation(summary = "Estadisticas Globales", description = "Resumen de goals a favor/contra por team")
    public ResponseEntity<?> getEstadisticas(@PathVariable String tournament) {
        log.info("REST request - getEstadisticas para tournament: {}", tournament);
        List<Standing> standingTable = tablaService.calcularTabla(tournament);
        if (standingTable.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay estadísticas disponibles para este tournament"));
        }
        return ResponseEntity.ok(standingTable);
    }

    @GetMapping("/{tournament}/goleadores")
    @Operation(summary = "Tabla de Goleadores", description = "Todos los máximos Goleadores agregados en matches")
    public ResponseEntity<?> getGoleadores(@PathVariable String tournament) {
        log.info("REST request - getGoleadores para tournament: {}", tournament);
        List<Map<String, Object>> goleadores = estadisticasService.getMaximosGoleadores(tournament);
        if (goleadores.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay goleadores registrados para este tournament"));
        }
        return ResponseEntity.ok(goleadores);
    }

    @GetMapping("/{tournament}/historial/{team}")
    @Operation(summary = "Historial Club", description = "Desempeño cronológico de un team dado")
    public ResponseEntity<?> getHistorialEquipo(@PathVariable String tournament, @PathVariable String team) {
        log.info("REST request - getHistorialEquipo para tournament: {}, team: {}", tournament, team);
        List<Map<String, Object>> historial = estadisticasService.getHistorialEquipo(tournament, team);
        if (historial.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay historial disponible para este team en el tournament"));
        }
        return ResponseEntity.ok(historial);
    }
}
