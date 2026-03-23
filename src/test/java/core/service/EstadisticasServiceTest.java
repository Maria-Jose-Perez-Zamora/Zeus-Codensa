package core.service;

import core.model.Match;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EstadisticasServiceTest {

    private StatisticsService estadisticasService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        estadisticasService = new StatisticsService();
    }

    @Test
    public void testGetMaximosGoleadores_Ordenados() {
        Match p1 = new Match("A", "B", "hoy", "Liga");
        p1.setStatus("FINISHED");
        p1.getGoles().put("jugador1@a.com", 2);
        p1.getGoles().put("jugador2@a.com", 1);

        Match p2 = new Match("A", "C", "ayer", "Liga");
        p2.setStatus("FINISHED");
        p2.getGoles().put("jugador1@a.com", 1); // jugador1 acumula 3 en total

        DataStorage.matches.add(p1);
        DataStorage.matches.add(p2);

        List<Map<String, Object>> result = estadisticasService.getTopScorers("Liga");
        assertEquals(2, result.size());
        assertEquals("jugador1@a.com", result.get(0).get("playerEmail"));
        assertEquals(3, result.get(0).get("goals"));
    }

    @Test
    public void testGetMaximosGoleadores_SinPartidosFinalizados_RetornaVacio() {
        Match p = new Match("A", "B", "hoy", "Liga");
        p.setStatus("SCHEDULED");
        DataStorage.matches.add(p);

        List<Map<String, Object>> result = estadisticasService.getTopScorers("Liga");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHistorialEquipo_MarcaResultado() {
        Match p1 = new Match("Tigres", "Leones", "hoy", "Liga");
        p1.setStatus("FINISHED");
        p1.setHomeScore(2);
        p1.setAwayScore(0);
        DataStorage.matches.add(p1);

        List<Map<String, Object>> historial = estadisticasService.getTeamHistory("Liga", "Tigres");
        assertEquals(1, historial.size());
        assertEquals("WIN", historial.get(0).get("result"));
        assertEquals("HOME", historial.get(0).get("venue"));
    }

    @Test
    public void testGetHistorialEquipo_VisitanteDerrota() {
        Match p1 = new Match("Tigres", "Leones", "hoy", "Liga");
        p1.setStatus("FINISHED");
        p1.setHomeScore(3);
        p1.setAwayScore(1);
        DataStorage.matches.add(p1);

        List<Map<String, Object>> historial = estadisticasService.getTeamHistory("Liga", "Leones");
        assertEquals("LOSS", historial.get(0).get("result"));
        assertEquals("AWAY", historial.get(0).get("venue"));
    }
}
