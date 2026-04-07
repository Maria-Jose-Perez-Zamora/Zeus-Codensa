package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.MatchService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartidoControllerTest {

    @Mock
    private MatchService partidoService;

    @InjectMocks
    private MatchController partidoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarPartido_Success() {
        MatchResponseDTO res = new MatchResponseDTO();
        when(partidoService.registrarPartido(any())).thenReturn(res);

        ResponseEntity<?> response = partidoController.registrarPartido(new MatchRequestDTO());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testActualizarMarcador_Success() {
        MatchResponseDTO res = new MatchResponseDTO();
        res.setHomeScore(2);
        when(partidoService.actualizarMarcador("1", 2, 1)).thenReturn(res);

        ResponseEntity<?> response = partidoController.actualizarMarcador("1", Map.of("homeScore", 2, "awayScore", 1));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarAlineacion_Success() {
        MatchResponseDTO res = new MatchResponseDTO();
        when(partidoService.registrarAlineacion(eq("1"), eq("TeamA"), anyList())).thenReturn(res);

        Map<String, Object> body = new HashMap<>();
        body.put("nombreEquipo", "TeamA");
        body.put("players", Arrays.asList("user.test1-a@escuelaing.edu.co", "user.test2-a@escuelaing.edu.co"));

        ResponseEntity<?> response = partidoController.registrarAlineacion("1", body);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarAlineacion_Error_Returns400() {
        when(partidoService.registrarAlineacion(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Equipo no participa"));

        Map<String, Object> body = new HashMap<>();
        body.put("nombreEquipo", "Otro");
        body.put("players", List.of("user.test3-a@escuelaing.edu.co"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> partidoController.registrarAlineacion("1", body));
        assertEquals("Equipo no participa", thrown.getMessage());
    }

    @Test
    public void testRegistrarTarjetas_Success() {
        MatchResponseDTO res = new MatchResponseDTO();
        when(partidoService.registrarTarjetas(eq("1"), anyMap(), anyMap())).thenReturn(res);

        Map<String, Object> body = new HashMap<>();
        body.put("yellowCards", new HashMap<>());
        body.put("redCards", new HashMap<>());

        ResponseEntity<?> response = partidoController.registrarTarjetas("1", body);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarTarjetas_Error_Returns400() {
        when(partidoService.registrarTarjetas(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Match no encontrado"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> partidoController.registrarTarjetas("999", new HashMap<>()));
        assertEquals("Match no encontrado", thrown.getMessage());
    }

    @Test
    public void testAsignarArbitro_Success() {
        MatchResponseDTO res = new MatchResponseDTO();
        res.setRefereeEmail("user.test4-a@escuelaing.edu.co");
        when(partidoService.asignarArbitro("1", "user.test4-a@escuelaing.edu.co")).thenReturn(res);

        ResponseEntity<?> response = partidoController.asignarArbitro("1", Map.of("correoArbitro", "user.test4-a@escuelaing.edu.co"));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testAsignarArbitro_NotArbitro_Returns400() {
        when(partidoService.asignarArbitro(any(), any()))
                .thenThrow(new IllegalArgumentException("No es árbitro"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> partidoController.asignarArbitro("1", Map.of("correoArbitro", "user.test5-a@escuelaing.edu.co")));
        assertEquals("No es árbitro", thrown.getMessage());
    }

    @Test
    public void testGetMisPartidos_Success() {
        when(partidoService.getMatchesByReferee("user.test4-a@escuelaing.edu.co")).thenReturn(Collections.emptyList());
        ResponseEntity<List<MatchResponseDTO>> response = partidoController.getMisPartidos("user.test4-a@escuelaing.edu.co");
        assertEquals(200, response.getStatusCode().value());
    }
}
