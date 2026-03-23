package controller;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Torneos", description = "Operaciones relacionadas con la creación y configuración de tournaments (RF-001, RF-005)")
public class TournamentController {

    private static final Logger log = LoggerFactory.getLogger(TournamentController.class);
    private final TournamentService torneoService;

    public TournamentController(TournamentService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear tournament", description = "Crea un tournament en status DRAFT con sus especificaciones iniciales")
    public ResponseEntity<TournamentResponseDTO> createTorneo(@RequestBody TournamentRequestDTO request) {
        log.info("REST request - createTorneo: {}", request.getTournamentName());
        return ResponseEntity.ok(torneoService.createTorneo(request));
    }

    @PutMapping("/{id}/configurar")
    @Operation(summary = "Configurar tournament", description = "Añade reglas, canchas, sanciones, horarios a un tournament existente")
    public ResponseEntity<TournamentResponseDTO> configurarTorneo(@PathVariable String id, @RequestBody TournamentRequestDTO configInfo) {
        log.info("REST request - configurarTorneo ID: {}", id);
        return ResponseEntity.ok(torneoService.configurarTorneo(id, configInfo));
    }

    @GetMapping("/consulta/all")
    @Operation(summary = "Consultar todos los tournaments", description = "Obtiene una lista de todos los tournaments registrados")
    public ResponseEntity<List<TournamentResponseDTO>> getAllTorneos() {
        log.info("REST request - getAllTorneos");
        return ResponseEntity.ok(torneoService.getAllTorneos());
    }
}
