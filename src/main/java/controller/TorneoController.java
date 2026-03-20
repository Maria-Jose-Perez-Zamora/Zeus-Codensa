package controller;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.TorneoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
@Tag(name = "Torneos", description = "Operaciones relacionadas con la creación y configuración de torneos (RF-001, RF-005)")
public class TorneoController {

    private static final Logger log = LoggerFactory.getLogger(TorneoController.class);
    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear torneo", description = "Crea un torneo en estado BORRADOR con sus especificaciones iniciales")
    public ResponseEntity<TorneoResponseDTO> createTorneo(@RequestBody TorneoRequestDTO request) {
        log.info("REST request - createTorneo: {}", request.getNombreTorneo());
        return ResponseEntity.ok(torneoService.createTorneo(request));
    }

    @PutMapping("/{id}/configurar")
    @Operation(summary = "Configurar torneo", description = "Añade reglas, canchas, sanciones, horarios a un torneo existente")
    public ResponseEntity<TorneoResponseDTO> configurarTorneo(@PathVariable String id, @RequestBody TorneoRequestDTO configInfo) {
        log.info("REST request - configurarTorneo ID: {}", id);
        return ResponseEntity.ok(torneoService.configurarTorneo(id, configInfo));
    }

    @GetMapping("/consulta/all")
    @Operation(summary = "Consultar todos los torneos", description = "Obtiene una lista de todos los torneos registrados")
    public ResponseEntity<List<TorneoResponseDTO>> getAllTorneos() {
        log.info("REST request - getAllTorneos");
        return ResponseEntity.ok(torneoService.getAllTorneos());
    }
}
