package controller;

import dto.PartidoRequestDTO;
import dto.PartidoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.PartidoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> registrarPartido(@RequestBody PartidoRequestDTO request) {
        try {
            return ResponseEntity.ok(partidoService.registrarPartido(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno en partidos");
        }
    }

    @PutMapping("/{id}/marcador")
    public ResponseEntity<?> actualizarMarcador(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        try {
            return ResponseEntity.ok(partidoService.actualizarMarcador(id, body.get("marcadorLocal"), body.get("marcadorVisitante")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * RF-008: Registrar alineación de un equipo para un partido.
     * PUT /api/partidos/{id}/alineacion
     * Body: { "nombreEquipo": "X", "jugadores": ["email1", "email2", ...] }
     */
    @PutMapping("/{id}/alineacion")
    public ResponseEntity<?> registrarAlineacion(@PathVariable String id, @RequestBody Map<String, Object> body) {
        try {
            String nombreEquipo = (String) body.get("nombreEquipo");
            @SuppressWarnings("unchecked")
            List<String> jugadores = (List<String>) body.get("jugadores");
            return ResponseEntity.ok(partidoService.registrarAlineacion(id, nombreEquipo, jugadores));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar alineación");
        }
    }

    /**
     * RF-008: Asignar árbitro a un partido.
     * PUT /api/partidos/{id}/arbitro
     * Body: { "correoArbitro": "arbitro@mail.com" }
     */
    @PutMapping("/{id}/arbitro")
    public ResponseEntity<?> asignarArbitro(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(partidoService.asignarArbitro(id, body.get("correoArbitro")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al asignar árbitro");
        }
    }

    /**
     * RF-008: Árbitro consulta sus partidos asignados ("mis partidos").
     * GET /api/partidos/arbitro/{correoArbitro}
     */
    @GetMapping("/arbitro/{correoArbitro}")
    public ResponseEntity<List<PartidoResponseDTO>> getMisPartidos(@PathVariable String correoArbitro) {
        return ResponseEntity.ok(partidoService.getPartidosPorArbitro(correoArbitro));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PartidoResponseDTO>> getAll() {
        return ResponseEntity.ok(partidoService.getAll());
    }
}
