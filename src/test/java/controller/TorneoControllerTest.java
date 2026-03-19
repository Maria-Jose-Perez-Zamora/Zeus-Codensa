package controller;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import service.TorneoService;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TorneoControllerTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private TorneoController torneoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTorneo_Success() {
        TorneoRequestDTO req = new TorneoRequestDTO("Liga", null, null, 8, 50.0);
        TorneoResponseDTO res = new TorneoResponseDTO();
        res.setNombreTorneo("Liga");

        when(torneoService.createTorneo(any())).thenReturn(res);

        ResponseEntity<?> response = torneoController.createTorneo(req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(res, response.getBody());
    }

    @Test
    public void testGetAllTorneos_Success() {
        when(torneoService.getAllTorneos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TorneoResponseDTO>> response = torneoController.getAllTorneos();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}
