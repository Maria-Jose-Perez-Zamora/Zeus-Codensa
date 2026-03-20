package controller;

import dto.InscripcionRequestDTO;
import dto.InscripcionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.InscripcionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "Proceso de abono de tarifa de inscripción al torneo (RF-004)")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/create")
    @Operation(summary = "Registrar Comprobante", description = "Sube el link/referencia del comprobante de pago NEQUI o efectivo")
    public ResponseEntity<?> createInscripcion(@RequestBody InscripcionRequestDTO request) {
        try {
            InscripcionResponseDTO response = inscripcionService.inscribir(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno en inscripciones");
        }
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Evaluar pago", description = "El organizador evalúa el comproabante para APROBAR o RECHAZAR la inscripción")
    public ResponseEntity<?> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            if (nuevoEstado == null) throw new IllegalArgumentException("Es necesario el campo 'estado'");
            
            InscripcionResponseDTO response = inscripcionService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Listar procesos", description = "Lista comprobantes pendientes y aprobados")
    public ResponseEntity<List<InscripcionResponseDTO>> getAll() {
        return ResponseEntity.ok(inscripcionService.getAll());
    }
}
