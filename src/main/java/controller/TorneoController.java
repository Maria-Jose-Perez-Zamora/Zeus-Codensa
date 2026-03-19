package controller;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.TorneoService;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTorneo(@RequestBody TorneoRequestDTO request) {
        try {
            TorneoResponseDTO response = torneoService.createTorneo(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno de torneo");
        }
    }

    @GetMapping("/consulta/all")
    public ResponseEntity<List<TorneoResponseDTO>> getAllTorneos() {
        return ResponseEntity.ok(torneoService.getAllTorneos());
    }
}
