package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationStatusUpdateRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.RegistrationMapper;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Registrations", description = "Payment process for tournament registration fees (RF-004)")
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @Operation(summary = "Register Receipt", description = "Uploads the link/reference of the payment receipt (NEQUI or cash)")
    public ResponseEntity<RegistrationResponseDTO> createInscripcion(@Valid @RequestBody RegistrationRequestDTO request) {
        log.info("REST request - createInscripcion para el team: {}", request.getTeamName());
        Registration model = RegistrationMapper.toEntity(request);
        Registration saved = registrationService.inscribir(model);
        return ResponseEntity.ok(RegistrationMapper.toDTO(saved));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Evaluate Payment", description = "The organizer evaluates the receipt to APPROVE or REJECT the registration")
    public ResponseEntity<RegistrationResponseDTO> actualizarEstado(@PathVariable String id, @Valid @RequestBody RegistrationStatusUpdateRequestDTO body) {
        log.info("REST request - actualizarEstado para inscripción ID: {}", id);
        Registration updated = registrationService.actualizarEstado(id, body.getStatus());
        return ResponseEntity.ok(RegistrationMapper.toDTO(updated));
    }

    @GetMapping
    @Operation(summary = "List Registrations", description = "Lists pending and approved receipts")
    public ResponseEntity<List<RegistrationResponseDTO>> getAll() {
        log.info("REST request - getAll Inscripciones");
        return ResponseEntity.ok(registrationService.getAll().stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList()));
    }
}
