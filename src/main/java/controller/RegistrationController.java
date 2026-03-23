package controller;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import core.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Inscripciones", description = "Proceso de abono de tarifa de inscripción al tournament (RF-004)")
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private final RegistrationService inscripcionService;

    public RegistrationController(RegistrationService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/create")
    @Operation(summary = "Registrar Comprobante", description = "Sube el link/referencia del comprobante de pago NEQUI o efectivo")
    public ResponseEntity<RegistrationResponseDTO> createInscripcion(@RequestBody RegistrationRequestDTO request) {
        log.info("REST request - createInscripcion para el team: {}", request.getNombreEquipo());
        return ResponseEntity.ok(inscripcionService.inscribir(request));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Evaluar pago", description = "El organizador evalúa el comproabante para APROBAR o RECHAZAR la inscripción")
    public ResponseEntity<RegistrationResponseDTO> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - actualizarEstado para inscripción ID: {}", id);
        String nuevoEstado = body.get("status");
        if (nuevoEstado == null) {
            log.warn("Falta el campo 'status' en el body de la petición");
            throw new IllegalArgumentException("Es necesario el campo 'status'");
        }
        return ResponseEntity.ok(inscripcionService.actualizarEstado(id, nuevoEstado));
    }

    @GetMapping("/all")
    @Operation(summary = "Listar procesos", description = "Lista comprobantes pendientes y aprobados")
    public ResponseEntity<List<RegistrationResponseDTO>> getAll() {
        log.info("REST request - getAll Inscripciones");
        return ResponseEntity.ok(inscripcionService.getAll());
    }
}
