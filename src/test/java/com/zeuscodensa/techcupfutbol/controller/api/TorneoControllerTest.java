package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.TournamentRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TournamentResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TournamentHistoryDTO;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.TournamentService;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TorneoControllerTest {

    @Mock
    private TournamentService torneoService;

    @InjectMocks
    private TournamentController torneoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTorneo_Success() {
        TournamentRequestDTO req = new TournamentRequestDTO("Liga", null, null, 8, 50.0);
        Tournament mockedTournament = new Tournament("Liga");

        when(torneoService.createTorneo(any(Tournament.class))).thenReturn(mockedTournament);

        ResponseEntity<TournamentResponseDTO> response = torneoController.createTorneo(req);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Liga", response.getBody().getTournamentName());
    }

    @Test
    public void testConfigurarTorneo_Success() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setRules("Rules");
        
        Tournament mockedTournament = new Tournament("Liga");
        mockedTournament.setRules("Rules");

        when(torneoService.configurarTorneo(eq("123"), any(Tournament.class))).thenReturn(mockedTournament);

        ResponseEntity<TournamentResponseDTO> response = torneoController.configurarTorneo("123", req);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Rules", response.getBody().getRules());
    }

    @Test
    public void testConfigurarTorneo_Error_Returns400() {
        when(torneoService.configurarTorneo(eq("invalid"), any(Tournament.class))).thenThrow(new IllegalArgumentException("Tournament no encontrado"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> torneoController.configurarTorneo("invalid", new TournamentRequestDTO()));
        assertEquals("Tournament no encontrado", thrown.getMessage());
    }

    @Test
    public void testGetAllTorneos_Success() {
        when(torneoService.getAllTorneos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TournamentHistoryDTO>> response = torneoController.getAllTorneos();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}
