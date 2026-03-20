package controller;

import dto.PartidoRequestDTO;
import dto.PartidoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import service.PartidoService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartidoControllerTest {

    @Mock
    private PartidoService partidoService;

    @InjectMocks
    private PartidoController partidoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarPartido_Success() {
        PartidoResponseDTO res = new PartidoResponseDTO();
        when(partidoService.registrarPartido(any())).thenReturn(res);

        ResponseEntity<?> response = partidoController.registrarPartido(new PartidoRequestDTO());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testActualizarMarcador_Success() {
        PartidoResponseDTO res = new PartidoResponseDTO();
        res.setMarcadorLocal(2);
        when(partidoService.actualizarMarcador("1", 2, 1)).thenReturn(res);

        ResponseEntity<?> response = partidoController.actualizarMarcador("1", Map.of("marcadorLocal", 2, "marcadorVisitante", 1));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarAlineacion_Success() {
        PartidoResponseDTO res = new PartidoResponseDTO();
        when(partidoService.registrarAlineacion(eq("1"), eq("TeamA"), anyList())).thenReturn(res);

        Map<String, Object> body = new HashMap<>();
        body.put("nombreEquipo", "TeamA");
        body.put("jugadores", Arrays.asList("j1@a.com", "j2@a.com"));

        ResponseEntity<?> response = partidoController.registrarAlineacion("1", body);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarAlineacion_Error_Returns400() {
        when(partidoService.registrarAlineacion(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Equipo no participa"));

        Map<String, Object> body = new HashMap<>();
        body.put("nombreEquipo", "Otro");
        body.put("jugadores", List.of("j@a.com"));

        ResponseEntity<?> response = partidoController.registrarAlineacion("1", body);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarTarjetas_Success() {
        PartidoResponseDTO res = new PartidoResponseDTO();
        when(partidoService.registrarTarjetas(eq("1"), anyMap(), anyMap())).thenReturn(res);

        Map<String, Object> body = new HashMap<>();
        body.put("tarjetasAmarillas", new HashMap<>());
        body.put("tarjetasRojas", new HashMap<>());

        ResponseEntity<?> response = partidoController.registrarTarjetas("1", body);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarTarjetas_Error_Returns400() {
        when(partidoService.registrarTarjetas(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Partido no encontrado"));

        ResponseEntity<?> response = partidoController.registrarTarjetas("999", new HashMap<>());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void testAsignarArbitro_Success() {
        PartidoResponseDTO res = new PartidoResponseDTO();
        res.setCorreoArbitro("ref@test.com");
        when(partidoService.asignarArbitro("1", "ref@test.com")).thenReturn(res);

        ResponseEntity<?> response = partidoController.asignarArbitro("1", Map.of("correoArbitro", "ref@test.com"));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testAsignarArbitro_NotArbitro_Returns400() {
        when(partidoService.asignarArbitro(any(), any()))
                .thenThrow(new IllegalArgumentException("No es árbitro"));

        ResponseEntity<?> response = partidoController.asignarArbitro("1", Map.of("correoArbitro", "x@a.com"));
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void testGetMisPartidos_Success() {
        when(partidoService.getPartidosPorArbitro("ref@test.com")).thenReturn(Collections.emptyList());
        ResponseEntity<List<PartidoResponseDTO>> response = partidoController.getMisPartidos("ref@test.com");
        assertEquals(200, response.getStatusCode().value());
    }
}
