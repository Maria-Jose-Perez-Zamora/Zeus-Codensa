package controller;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.TournamentService;
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
        TournamentResponseDTO res = new TournamentResponseDTO();
        res.setTournamentName("Liga");

        when(torneoService.createTorneo(any())).thenReturn(res);

        ResponseEntity<?> response = torneoController.createTorneo(req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(res, response.getBody());
    }

    @Test
    public void testConfigurarTorneo_Success() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setReglamento("Rules");
        
        TournamentResponseDTO res = new TournamentResponseDTO();
        res.setReglamento("Rules");
        res.setTournamentName("Liga");

        when(torneoService.configurarTorneo(eq("123"), any())).thenReturn(res);

        ResponseEntity<?> response = torneoController.configurarTorneo("123", req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(res, response.getBody());
    }

    @Test
    public void testConfigurarTorneo_Error_Returns400() {
        when(torneoService.configurarTorneo(eq("invalid"), any())).thenThrow(new IllegalArgumentException("Tournament no encontrado"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> torneoController.configurarTorneo("invalid", new TournamentRequestDTO()));
        assertEquals("Tournament no encontrado", thrown.getMessage());
    }

    @Test
    public void testGetAllTorneos_Success() {
        when(torneoService.getAllTorneos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TournamentResponseDTO>> response = torneoController.getAllTorneos();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}
