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
@Tag(name = "Partidos", description = "Manejo de matches, resultados, alineaciones, tarjetas y árbitros (RF-006, RF-008)")
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);
    private final MatchService partidoService;

    public MatchController(MatchService partidoService) {
        this.partidoService = partidoService;
    }

    @PostMapping("/create")
    public ResponseEntity<MatchResponseDTO> registrarPartido(@RequestBody MatchRequestDTO request) {
        log.info("REST request - registrarPartido");
        return ResponseEntity.ok(partidoService.registrarPartido(request));
    }

    @PutMapping("/{id}/marcador")
    public ResponseEntity<MatchResponseDTO> actualizarMarcador(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        log.info("REST request - actualizarMarcador para el match ID: {}", id);
        return ResponseEntity.ok(partidoService.actualizarMarcador(id, body.get("homeScore"), body.get("awayScore")));
    }

    @PutMapping("/{id}/alineacion")
    @Operation(summary = "Registrar alineación", description = "Registra los players que jugarán en un team en un match específico")
    public ResponseEntity<MatchResponseDTO> registrarAlineacion(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarAlineacion para el match ID: {}", id);
        String nombreEquipo = (String) body.get("nombreEquipo");
        @SuppressWarnings("unchecked")
        List<String> players = (List<String>) body.get("players");
        return ResponseEntity.ok(partidoService.registrarAlineacion(id, nombreEquipo, players));
    }

    @PutMapping("/{id}/tarjetas")
    @Operation(summary = "Registrar tarjetas", description = "Añade tarjetas amarillas y rojas al match por player")
    public ResponseEntity<MatchResponseDTO> registrarTarjetas(@PathVariable String id, @RequestBody Map<String, Object> body) {
        log.info("REST request - registrarTarjetas para el match ID: {}", id);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> amarillas = (Map<String, List<String>>) body.get("yellowCards");
        @SuppressWarnings("unchecked")
        Map<String, List<String>> rojas = (Map<String, List<String>>) body.get("redCards");
        return ResponseEntity.ok(partidoService.registrarTarjetas(id, amarillas, rojas));
    }

    @PutMapping("/{id}/arbitro")
    public ResponseEntity<MatchResponseDTO> asignarArbitro(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - asignarArbitro para el match ID: {}", id);
        return ResponseEntity.ok(partidoService.asignarArbitro(id, body.get("correoArbitro")));
    }

    @GetMapping("/arbitro/{correoArbitro}")
    @Operation(summary = "Obtener mis matches", description = "Retorna todos los matches asignados a un árbitro")
    public ResponseEntity<List<MatchResponseDTO>> getMisPartidos(@PathVariable String correoArbitro) {
        log.info("REST request - getMisPartidos para el árbitro: {}", correoArbitro);
        return ResponseEntity.ok(partidoService.getPartidosPorArbitro(correoArbitro));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MatchResponseDTO>> getAll() {
        log.info("REST request - getAll Partidos");
        return ResponseEntity.ok(partidoService.getAll());
    }
}
