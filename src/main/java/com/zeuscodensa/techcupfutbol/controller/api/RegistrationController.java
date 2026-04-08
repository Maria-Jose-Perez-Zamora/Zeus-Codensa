package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.RegistrationMapper;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zeuscodensa.techcupfutbol.core.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Registrations", description = "Payment process for tournament registration fees (RF-004)")
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private final RegistrationService inscripcionService;

    public RegistrationController(RegistrationService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    @Operation(summary = "Register Receipt", description = "Uploads the link/reference of the payment receipt (NEQUI or cash)")
    public ResponseEntity<RegistrationResponseDTO> createInscripcion(@RequestBody RegistrationRequestDTO request) {
        log.info("REST request - createInscripcion para el team: {}", request.getTeamName());
        Registration model = RegistrationMapper.toEntity(request);
        Registration saved = inscripcionService.inscribir(model);
        return ResponseEntity.ok(RegistrationMapper.toDTO(saved));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Evaluate Payment", description = "The organizer evaluates the receipt to APPROVE or REJECT the registration")
    public ResponseEntity<RegistrationResponseDTO> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        log.info("REST request - actualizarEstado para inscripción ID: {}", id);
        String nuevoEstado = body.get("status");
        if (nuevoEstado == null) {
            log.warn("Falta el campo 'status' en el body de la petición");
            throw new IllegalArgumentException("Es necesario el campo 'status'");
        }
        Registration updated = inscripcionService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(RegistrationMapper.toDTO(updated));
    }

    @GetMapping
    @Operation(summary = "List Registrations", description = "Lists pending and approved receipts")
    public ResponseEntity<List<RegistrationResponseDTO>> getAll() {
        log.info("REST request - getAll Inscripciones");
        return ResponseEntity.ok(inscripcionService.getAll().stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList()));
    }
}
