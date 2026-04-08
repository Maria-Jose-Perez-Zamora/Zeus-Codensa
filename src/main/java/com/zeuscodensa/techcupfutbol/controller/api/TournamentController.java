package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.TournamentRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TournamentResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TournamentHistoryDTO;
import com.zeuscodensa.techcupfutbol.controller.mapper.TournamentMapper;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zeuscodensa.techcupfutbol.core.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Tournaments", description = "Operations related to tournament creation and configuration (RF-001, RF-005)")
public class TournamentController {

    private static final Logger log = LoggerFactory.getLogger(TournamentController.class);
    private final TournamentService torneoService;

    public TournamentController(TournamentService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping
    @Operation(summary = "Create Tournament", description = "Creates a tournament in DRAFT status with initial specifications")
    public ResponseEntity<TournamentResponseDTO> createTorneo(@RequestBody TournamentRequestDTO request) {
        log.info("REST request - createTorneo: {}", request.getTournamentName());
        Tournament newTournament = TournamentMapper.toEntity(request);
        Tournament saved = torneoService.createTorneo(newTournament);
        return ResponseEntity.ok(TournamentMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Configure Tournament", description = "Adds rules, fields, sanctions, and schedules to an existing tournament")
    public ResponseEntity<TournamentResponseDTO> configurarTorneo(@PathVariable String id, @RequestBody TournamentRequestDTO configInfo) {
        log.info("REST request - configurarTorneo ID: {}", id);
        Tournament configModel = TournamentMapper.toEntity(configInfo);
        Tournament saved = torneoService.configurarTorneo(id, configModel);
        return ResponseEntity.ok(TournamentMapper.toDTO(saved));
    }

    @GetMapping("/query/all")
    @Operation(summary = "Get Tournament History", description = "Gets an optimized list of all registered tournaments with essential footprint (name, dates, status, champion)")
    public ResponseEntity<List<TournamentHistoryDTO>> getAllTorneos() {
        log.info("REST request - getAllTorneos");
        return ResponseEntity.ok(torneoService.getAllTorneos().stream()
                .map(TournamentHistoryDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/query/{id}")
    @Operation(summary = "Get Tournament Details", description = "Gets full detailed configuration of a specific tournament by ID")
    public ResponseEntity<TournamentResponseDTO> getTorneoById(@PathVariable String id) {
        log.info("REST request - getTorneoById: {}", id);
        return ResponseEntity.ok(TournamentMapper.toDTO(torneoService.getTorneoById(id)));
    }
}
