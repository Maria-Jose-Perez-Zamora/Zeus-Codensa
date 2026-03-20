package controller;

import model.TablaPosicion;
import model.Partido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.EstadisticasService;
import service.LlaveService;
import service.TablaService;
import util.DataStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/torneos/consulta")
@Tag(name = "Consulta e Información del Torneo", description = "Visualización de Tablas, Estadísticas, Llaves eliminatorias y Calendarios (RF-007, RF-008)")
public class ConsultaTorneoController {

    private final TablaService tablaService;
    private final LlaveService llaveService;
    private final EstadisticasService estadisticasService;

    public ConsultaTorneoController(TablaService tablaService, LlaveService llaveService, EstadisticasService estadisticasService) {
        this.tablaService = tablaService;
        this.llaveService = llaveService;
        this.estadisticasService = estadisticasService;
    }

    /** RF-008: Tabla de posiciones */
    @GetMapping("/{torneo}/tabla")
    @Operation(summary = "Tabla de posiciones", description = "Genera la tabla general a partir del torneo activo sumando victorias y empates")
    public ResponseEntity<List<TablaPosicion>> getTabla(@PathVariable String torneo) {
        return ResponseEntity.ok(tablaService.calcularTabla(torneo));
    }

    /** RF-008: Llaves eliminatorias */
    @GetMapping("/{torneo}/llaves/{fase}")
    @Operation(summary = "Generación Llaves Eliminatorias", description = "Dibuja cuartos, semifinal o final")
    public ResponseEntity<?> getLlaves(@PathVariable String torneo, @PathVariable String fase) {
        try {
            return ResponseEntity.ok(llaveService.generarLlaves(torneo, fase));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error consolidando info");
        }
    }

    /** RF-008: Calendario — partidos PROGRAMADOS */
    @GetMapping("/{torneo}/calendario")
    @Operation(summary = "Calendario Partidos", description = "Devuelve los partidos programados")
    public ResponseEntity<?> getCalendario(@PathVariable String torneo) {
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

    /** RF-008: Resultados — partidos FINALIZADOS con marcador */
    @GetMapping("/{torneo}/resultados")
    @Operation(summary = "Resultados Historicos", description = "Todos los partidos ya finalizados")
    public ResponseEntity<?> getResultados(@PathVariable String torneo) {
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

    /** RF-008: Estadísticas — tabla de posiciones completa */
    @GetMapping("/{torneo}/estadisticas")
    @Operation(summary = "Estadisticas Globales", description = "Resumen de goles a favor/contra por equipo")
    public ResponseEntity<?> getEstadisticas(@PathVariable String torneo) {
        List<TablaPosicion> tabla = tablaService.calcularTabla(torneo);
        if (tabla.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay estadísticas disponibles para este torneo"));
        }
        return ResponseEntity.ok(tabla);
    }

    /** RF-008: Máximos goleadores del torneo */
    @GetMapping("/{torneo}/goleadores")
    @Operation(summary = "Tabla de Goleadores", description = "Todos los máximos Goleadores agregados en partidos")
    public ResponseEntity<?> getGoleadores(@PathVariable String torneo) {
        List<Map<String, Object>> goleadores = estadisticasService.getMaximosGoleadores(torneo);
        if (goleadores.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay goleadores registrados para este torneo"));
        }
        return ResponseEntity.ok(goleadores);
    }

    /** RF-008: Historial de partidos de un equipo en el torneo */
    @GetMapping("/{torneo}/historial/{equipo}")
    @Operation(summary = "Historial Club", description = "Desempeño cronológico de un equipo dado")
    public ResponseEntity<?> getHistorialEquipo(@PathVariable String torneo, @PathVariable String equipo) {
        List<Map<String, Object>> historial = estadisticasService.getHistorialEquipo(torneo, equipo);
        if (historial.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensaje", "No hay historial disponible para este equipo en el torneo"));
        }
        return ResponseEntity.ok(historial);
    }
}
