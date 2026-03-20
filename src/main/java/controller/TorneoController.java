package controller;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.TorneoService;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
@Tag(name = "Torneos", description = "Operaciones relacionadas con la creación y configuración de torneos (RF-001, RF-005)")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear torneo", description = "Crea un torneo en estado BORRADOR con sus especificaciones iniciales")
    public ResponseEntity<?> createTorneo(@RequestBody TorneoRequestDTO request) {
        try {
            return ResponseEntity.ok(torneoService.createTorneo(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno de torneo");
        }
    }

    /**
     * RF-005: Advanced Configuration for Tournaments
     */
    @PutMapping("/{id}/configurar")
    @Operation(summary = "Configurar torneo", description = "Añade reglas, canchas, sanciones, horarios a un torneo existente")
    public ResponseEntity<?> configurarTorneo(@PathVariable String id, @RequestBody TorneoRequestDTO configInfo) {
        try {
            return ResponseEntity.ok(torneoService.configurarTorneo(id, configInfo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno al configurar el torneo");
        }
    }

    @GetMapping("/consulta/all")
    @Operation(summary = "Consultar todos los torneos", description = "Obtiene una lista de todos los torneos registrados")
    public ResponseEntity<List<TorneoResponseDTO>> getAllTorneos() {
        return ResponseEntity.ok(torneoService.getAllTorneos());
    }
}
