package controller;

import dto.PartidoRequestDTO;
import dto.PartidoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.PartidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partidos")
@Tag(name = "Partidos", description = "Manejo de partidos, resultados, alineaciones, tarjetas y árbitros (RF-006, RF-008)")
public class PartidoController {

    private static final Logger log = LoggerFactory.getLogger(PartidoController.class);
    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @PostMapping("/create")
    public ResponseEntity<PartidoResponseDTO> registrarPartido(@RequestBody PartidoRequestDTO request) {
        log.info("REST request - registrarPartido");
        return ResponseEntity.ok(partidoService.registrarPartido(request));
    }

    @PutMapping("/{id}/marcador")
    public ResponseEntity<PartidoResponseDTO> actualizarMarcador(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        log.info("REST request - actualizarMarcador para el partido ID: {}", id);
        return ResponseEntity.ok(partidoService.actualizarMarcador(id, body.get("marcadorLocal"), body.get("marcadorVisitante")));
    }

    @PutMapping("/{id}/alineacion")
    @Operation(summary = "Registrar alineación", description = "Registra los jugadores que jugarán en un equipo en un partido específico")
    public ResponseEntity<PartidoResponseDTO> registrarAlineacion(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarAlineacion para el partido ID: {}", id);
        String nombreEquipo = (String) body.get("nombreEquipo");
        @SuppressWarnings("unchecked")
        List<String> jugadores = (List<String>) body.get("jugadores");
        return ResponseEntity.ok(partidoService.registrarAlineacion(id, nombreEquipo, jugadores));
    }

    @PutMapping("/{id}/tarjetas")
    @Operation(summary = "Registrar tarjetas", description = "Añade tarjetas amarillas y rojas al partido por jugador")
    public ResponseEntity<PartidoResponseDTO> registrarTarjetas(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarTarjetas para el partido ID: {}", id);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> amarillas = (Map<String, List<String>>) body.get("tarjetasAmarillas");
        @SuppressWarnings("unchecked")
        Map<String, List<String>> rojas = (Map<String, List<String>>) body.get("tarjetasRojas");
        return ResponseEntity.ok(partidoService.registrarTarjetas(id, amarillas, rojas));
    }

    @PutMapping("/{id}/arbitro")
    public ResponseEntity<PartidoResponseDTO> asignarArbitro(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - asignarArbitro para el partido ID: {}", id);
        return ResponseEntity.ok(partidoService.asignarArbitro(id, body.get("correoArbitro")));
    }

    @GetMapping("/arbitro/{correoArbitro}")
    @Operation(summary = "Obtener mis partidos", description = "Retorna todos los partidos asignados a un árbitro")
    public ResponseEntity<List<PartidoResponseDTO>> getMisPartidos(@PathVariable String correoArbitro) {
        log.info("REST request - getMisPartidos para el árbitro: {}", correoArbitro);
        return ResponseEntity.ok(partidoService.getPartidosPorArbitro(correoArbitro));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PartidoResponseDTO>> getAll() {
        log.info("REST request - getAll Partidos");
        return ResponseEntity.ok(partidoService.getAll());
    }
}
