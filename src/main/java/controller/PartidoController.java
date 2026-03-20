package controller;

import dto.PartidoRequestDTO;
import dto.PartidoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import service.PartidoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partidos")
@Tag(name = "Partidos", description = "Manejo de partidos, resultados, alineaciones, tarjetas y árbitros (RF-006, RF-008)")
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
     */
    @PutMapping("/{id}/alineacion")
    @Operation(summary = "Registrar alineación", description = "Registra los jugadores que jugarán en un equipo en un partido específico")
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
     * RF-006: Registrar tarjetas amarillas y rojas (por equipo/jugador)
     */
    @PutMapping("/{id}/tarjetas")
    @Operation(summary = "Registrar tarjetas", description = "Añade tarjetas amarillas y rojas al partido por jugador")
    public ResponseEntity<?> registrarTarjetas(@PathVariable String id, @RequestBody Map<String, Object> body) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, List<String>> amarillas = (Map<String, List<String>>) body.get("tarjetasAmarillas");
            @SuppressWarnings("unchecked")
            Map<String, List<String>> rojas = (Map<String, List<String>>) body.get("tarjetasRojas");
            return ResponseEntity.ok(partidoService.registrarTarjetas(id, amarillas, rojas));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar tarjetas");
        }
    }

    /**
     * RF-008: Asignar árbitro a un partido.
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
     */
    @GetMapping("/arbitro/{correoArbitro}")
    @Operation(summary = "Obtener mis partidos", description = "Retorna todos los partidos asignados a un árbitro")
    public ResponseEntity<List<PartidoResponseDTO>> getMisPartidos(@PathVariable String correoArbitro) {
        return ResponseEntity.ok(partidoService.getPartidosPorArbitro(correoArbitro));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PartidoResponseDTO>> getAll() {
        return ResponseEntity.ok(partidoService.getAll());
    }
}
