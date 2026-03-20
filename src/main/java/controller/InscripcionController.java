package controller;

import dto.InscripcionRequestDTO;
import dto.InscripcionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.InscripcionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "Proceso de abono de tarifa de inscripción al torneo (RF-004)")
public class InscripcionController {

    private static final Logger log = LoggerFactory.getLogger(InscripcionController.class);
    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/create")
    @Operation(summary = "Registrar Comprobante", description = "Sube el link/referencia del comprobante de pago NEQUI o efectivo")
    public ResponseEntity<InscripcionResponseDTO> createInscripcion(@RequestBody InscripcionRequestDTO request) {
        log.info("REST request - createInscripcion para el equipo: {}", request.getNombreEquipo());
        return ResponseEntity.ok(inscripcionService.inscribir(request));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Evaluar pago", description = "El organizador evalúa el comproabante para APROBAR o RECHAZAR la inscripción")
    public ResponseEntity<InscripcionResponseDTO> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - actualizarEstado para inscripción ID: {}", id);
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null) {
            log.warn("Falta el campo 'estado' en el body de la petición");
            throw new IllegalArgumentException("Es necesario el campo 'estado'");
        }
        return ResponseEntity.ok(inscripcionService.actualizarEstado(id, nuevoEstado));
    }

    @GetMapping("/all")
    @Operation(summary = "Listar procesos", description = "Lista comprobantes pendientes y aprobados")
    public ResponseEntity<List<InscripcionResponseDTO>> getAll() {
        log.info("REST request - getAll Inscripciones");
        return ResponseEntity.ok(inscripcionService.getAll());
    }
}
