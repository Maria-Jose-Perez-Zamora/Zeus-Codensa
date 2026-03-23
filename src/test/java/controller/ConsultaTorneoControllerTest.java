package controller;

import core.model.Match;
import core.model.Standing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.StatisticsService;
import core.service.BracketService;
import core.service.StandingService;
import dependencies.util.DataStorage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaTorneoControllerTest {

    @Mock
    private StandingService tablaService;

    @Mock
    private BracketService llaveService;

    @Mock
    private StatisticsService estadisticasService;

    @InjectMocks
    private TournamentQueryController consultaTorneoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        DataStorage.clearAll();
    }

    @Test
    public void testGetTabla_Success() {
        when(tablaService.calcularTabla("Liga")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getTabla("Liga");
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testGetLlaves_Success() {
        when(llaveService.generarLlaves("Liga", "Semi")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getLlaves("Liga", "Semi");
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testGetCalendario_SinPartidos_ReturnsMensaje() {
        ResponseEntity<?> res = consultaTorneoController.getCalendario("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay matches programados"));
    }

    @Test
    public void testGetCalendario_ConPartidos_ReturnsLista() {
        Match p = new Match("A", "B", "2026-06-01", "LigaX");
        p.setStatus("SCHEDULED");
        DataStorage.matches.add(p);

        ResponseEntity<?> res = consultaTorneoController.getCalendario("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetResultados_SinPartidos_ReturnsMensaje() {
        ResponseEntity<?> res = consultaTorneoController.getResultados("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay resultados"));
    }

    @Test
    public void testGetResultados_ConPartidoFinalizado() {
        Match p = new Match("A", "B", "2026-06-01", "LigaX");
        p.setStatus("FINISHED");
        p.setHomeScore(2);
        p.setAwayScore(1);
        DataStorage.matches.add(p);

        ResponseEntity<?> res = consultaTorneoController.getResultados("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetEstadisticas_SinDatos_ReturnsMensaje() {
        when(tablaService.calcularTabla("LigaX")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getEstadisticas("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay estadísticas"));
    }

    @Test
    public void testGetEstadisticas_ConDatos_ReturnsLista() {
        Standing tp = new Standing("Equipo A");
        when(tablaService.calcularTabla("LigaX")).thenReturn(List.of(tp));
        ResponseEntity<?> res = consultaTorneoController.getEstadisticas("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetGoleadores_SinDatos_ReturnsMensaje() {
        when(estadisticasService.getMaximosGoleadores("LigaX")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getGoleadores("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay goleadores"));
    }

    @Test
    public void testGetGoleadores_ConDatos_ReturnsLista() {
        when(estadisticasService.getMaximosGoleadores("LigaX")).thenReturn(List.of(Map.of("name", "Juan")));
        ResponseEntity<?> res = consultaTorneoController.getGoleadores("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetHistorialEquipo_SinDatos_ReturnsMensaje() {
        when(estadisticasService.getHistorialEquipo("LigaX", "EquipoA")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getHistorialEquipo("LigaX", "EquipoA");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay historial"));
    }

    @Test
    public void testGetHistorialEquipo_ConDatos_ReturnsLista() {
        when(estadisticasService.getHistorialEquipo("LigaX", "EquipoA")).thenReturn(List.of(Map.of("id", 1)));
        ResponseEntity<?> res = consultaTorneoController.getHistorialEquipo("LigaX", "EquipoA");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }
}
