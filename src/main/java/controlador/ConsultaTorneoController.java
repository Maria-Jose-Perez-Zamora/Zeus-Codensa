package controlador;

import core.model.TablaPosicion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.EstadisticasService;
import core.service.LlaveService;
import core.service.TablaService;
import dependencias.util.DataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/torneos/consulta")
@Tag(name = "Consulta e Información del Torneo", description = "Visualización de Tablas, Estadísticas, Llaves eliminatorias y Calendarios (RF-007, RF-008)")
public class ConsultaTorneoController {

    private static final Logger log = LoggerFactory.getLogger(ConsultaTorneoController.class);

    private final TablaService tablaService;
    private final LlaveService llaveService;
    private final EstadisticasService estadisticasService;

    public ConsultaTorneoController(TablaService tablaService, LlaveService llaveService, EstadisticasService estadisticasService) {
        this.tablaService = tablaService;
        this.llaveService = llaveService;
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("")
    @Operation(summary = "Indice de Consultas", description = "Endpoint base que valida conexion")
    public ResponseEntity<Map<String, String>> index() {
        return ResponseEntity.ok(Map.of(
            "status", "Consulta API is running",
            "message", "Usa /{torneo}/tabla, /{torneo}/llaves/{fase}, /{torneo}/calendario, /{torneo}/estadisticas, /{torneo}/goleadores"
        ));
    }

    @GetMapping("/{torneo}/tabla")
    @Operation(summary = "Tabla de posiciones", description = "Genera la tabla general a partir del torneo activo sumando victorias y empates")
    public ResponseEntity<List<TablaPosicion>> getTabla(@PathVariable String torneo) {
        log.info("REST request - getTabla de posiciones para torneo: {}", torneo);
        return ResponseEntity.ok(tablaService.calcularTabla(torneo));
    }

    @GetMapping("/{torneo}/llaves/{fase}")
    @Operation(summary = "Generación Llaves Eliminatorias", description = "Dibuja cuartos, semifinal o final")
    public ResponseEntity<?> getLlaves(@PathVariable String torneo, @PathVariable String fase) {
        log.info("REST request - getLlaves eliminatorias para torneo: {}, fase: {}", torneo, fase);
        return ResponseEntity.ok(llaveService.generarLlaves(torneo, fase));
    }

    @GetMapping("/{torneo}/calendario")
    @Operation(summary = "Calendario Partidos", description = "Devuelve los partidos programados")
    public ResponseEntity<?> getCalendario(@PathVariable String torneo) {
        log.info("REST request - getCalendario para torneo: {}", torneo);
        List<Map<String, Object>> calendario = DataStorage.partidos.stream()
                .filter(p -> p.getNombreTorneo().equals(torneo) && "PROGRAMADO".equals(p.getEstado()))
                .map(p -> Map.<String, Object>of(
                        "id", p.getId(),
                        "equipoLocal", p.getEquipoLocal(),
                        "equipoVisitante", p.getEquipoVisitante(),
                        "fechaPartido", p.getFechaPartido(),
                        "estado", p.getEstado()
                ))
                .collect(Collectors.toList());

        if (calendario.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay partidos programados para este torneo"));
        }
        return ResponseEntity.ok(calendario);
    }

    @GetMapping("/{torneo}/resultados")
    @Operation(summary = "Resultados Historicos", description = "Todos los partidos ya finalizados")
    public ResponseEntity<?> getResultados(@PathVariable String torneo) {
        log.info("REST request - getResultados Historicos para torneo: {}", torneo);
        List<Map<String, Object>> resultados = DataStorage.partidos.stream()
                .filter(p -> p.getNombreTorneo().equals(torneo) && "FINALIZADO".equals(p.getEstado()))
                .map(p -> Map.<String, Object>of(
                        "id", p.getId(),
                        "equipoLocal", p.getEquipoLocal(),
                        "equipoVisitante", p.getEquipoVisitante(),
                        "marcadorLocal", p.getMarcadorLocal(),
                        "marcadorVisitante", p.getMarcadorVisitante(),
                        "fechaPartido", p.getFechaPartido()
                ))
                .collect(Collectors.toList());

        if (resultados.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay resultados disponibles para este torneo"));
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{torneo}/estadisticas")
    @Operation(summary = "Estadisticas Globales", description = "Resumen de goles a favor/contra por equipo")
    public ResponseEntity<?> getEstadisticas(@PathVariable String torneo) {
        log.info("REST request - getEstadisticas para torneo: {}", torneo);
        List<TablaPosicion> tabla = tablaService.calcularTabla(torneo);
        if (tabla.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay estadísticas disponibles para este torneo"));
        }
        return ResponseEntity.ok(tabla);
    }

    @GetMapping("/{torneo}/goleadores")
    @Operation(summary = "Tabla de Goleadores", description = "Todos los máximos Goleadores agregados en partidos")
    public ResponseEntity<?> getGoleadores(@PathVariable String torneo) {
        log.info("REST request - getGoleadores para torneo: {}", torneo);
        List<Map<String, Object>> goleadores = estadisticasService.getMaximosGoleadores(torneo);
        if (goleadores.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay goleadores registrados para este torneo"));
        }
        return ResponseEntity.ok(goleadores);
    }

    @GetMapping("/{torneo}/historial/{equipo}")
    @Operation(summary = "Historial Club", description = "Desempeño cronológico de un equipo dado")
    public ResponseEntity<?> getHistorialEquipo(@PathVariable String torneo, @PathVariable String equipo) {
        log.info("REST request - getHistorialEquipo para torneo: {}, equipo: {}", torneo, equipo);
        List<Map<String, Object>> historial = estadisticasService.getHistorialEquipo(torneo, equipo);
        if (historial.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay historial disponible para este equipo en el torneo"));
        }
        return ResponseEntity.ok(historial);
    }
}
