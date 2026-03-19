package controller;

import dto.InscripcionRequestDTO;
import dto.InscripcionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.InscripcionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/create")
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
    public ResponseEntity<List<InscripcionResponseDTO>> getAll() {
        return ResponseEntity.ok(inscripcionService.getAll());
    }
}
