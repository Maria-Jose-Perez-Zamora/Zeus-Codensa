package controller;

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
import dependencies.persistence.entity.MatchEntity;
import dependencies.persistence.repository.MatchRepository;

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

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private TournamentQueryController consultaTorneoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
        ResponseEntity<?> res = consultaTorneoController.getBrackets("Liga", "Semi");
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testGetCalendario_SinPartidos_ReturnsMensaje() {
        ResponseEntity<?> res = consultaTorneoController.getCalendar("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No scheduled matches"));
    }

    @Test
    public void testGetCalendario_ConPartidos_ReturnsLista() {
        MatchEntity p = new MatchEntity();
        p.setId("1");
        p.setHomeTeam("A");
        p.setAwayTeam("B");
        p.setMatchDate("2026-06-01");
        p.setTournamentName("LigaX");
        p.setStatus("SCHEDULED");
        
        when(matchRepository.findByTournamentName("LigaX")).thenReturn(Collections.singletonList(p));

        ResponseEntity<?> res = consultaTorneoController.getCalendar("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetResultados_SinPartidos_ReturnsMensaje() {
        ResponseEntity<?> res = consultaTorneoController.getResultados("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No results"));
    }

    @Test
    public void testGetResultados_ConPartidoFinalizado() {
        MatchEntity p = new MatchEntity();
        p.setId("2");
        p.setHomeTeam("A");
        p.setAwayTeam("B");
        p.setMatchDate("2026-06-01");
        p.setTournamentName("LigaX");
        p.setStatus("FINISHED");
        p.setHomeScore(2);
        p.setAwayScore(1);
        
        when(matchRepository.findByTournamentName("LigaX")).thenReturn(Collections.singletonList(p));

        ResponseEntity<?> res = consultaTorneoController.getResultados("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetEstadisticas_SinDatos_ReturnsMensaje() {
        when(tablaService.calcularTabla("LigaX")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getEstadisticas("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No statistics available"));
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
        when(estadisticasService.getTopScorers("LigaX")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getScorers("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No scorers registered"));
    }

    @Test
    public void testGetGoleadores_ConDatos_ReturnsLista() {
        when(estadisticasService.getTopScorers("LigaX")).thenReturn(List.of(Map.of("name", "Juan")));
        ResponseEntity<?> res = consultaTorneoController.getScorers("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetHistorialEquipo_SinDatos_ReturnsMensaje() {
        when(estadisticasService.getTeamHistory("LigaX", "EquipoA")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getTeamHistory("LigaX", "EquipoA");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No history"));
    }

    @Test
    public void testGetHistorialEquipo_ConDatos_ReturnsLista() {
        when(estadisticasService.getTeamHistory("LigaX", "EquipoA")).thenReturn(List.of(Map.of("id", 1)));
        ResponseEntity<?> res = consultaTorneoController.getTeamHistory("LigaX", "EquipoA");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }
}
