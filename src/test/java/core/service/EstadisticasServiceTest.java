package core.service;

import dependencies.persistence.entity.MatchEntity;
import dependencies.persistence.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstadisticasServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private StatisticsService estadisticasService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetMaximosGoleadores_Ordenados() {
        MatchEntity p1 = new MatchEntity();
        p1.setHomeTeam("A");
        p1.setAwayTeam("B");
        p1.setMatchDate("hoy");
        p1.setTournamentName("Liga");
        p1.setStatus("FINISHED");
        p1.setGoalsJson("{\"user.test1-a@escuelaing.edu.co\":2,\"user.test2-a@escuelaing.edu.co\":1}");

        MatchEntity p2 = new MatchEntity();
        p2.setHomeTeam("A");
        p2.setAwayTeam("C");
        p2.setMatchDate("ayer");
        p2.setTournamentName("Liga");
        p2.setStatus("FINISHED");
        p2.setGoalsJson("{\"user.test1-a@escuelaing.edu.co\":1}"); // jugador1 acumula 3 en total

        when(matchRepository.findByTournamentName("Liga")).thenReturn(Arrays.asList(p1, p2));

        List<Map<String, Object>> result = estadisticasService.getTopScorers("Liga");
        assertEquals(2, result.size());
        assertEquals("user.test1-a@escuelaing.edu.co", result.get(0).get("playerEmail"));
        assertEquals(3, result.get(0).get("goals"));
    }

    @Test
    public void testGetMaximosGoleadores_SinPartidosFinalizados_RetornaVacio() {
        MatchEntity p = new MatchEntity();
        p.setHomeTeam("A");
        p.setAwayTeam("B");
        p.setMatchDate("hoy");
        p.setTournamentName("Liga");
        p.setStatus("SCHEDULED");
        
        when(matchRepository.findByTournamentName("Liga")).thenReturn(Collections.singletonList(p));

        List<Map<String, Object>> result = estadisticasService.getTopScorers("Liga");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHistorialEquipo_MarcaResultado() {
        MatchEntity p1 = new MatchEntity();
        p1.setHomeTeam("Tigres");
        p1.setAwayTeam("Leones");
        p1.setMatchDate("hoy");
        p1.setTournamentName("Liga");
        p1.setStatus("FINISHED");
        p1.setHomeScore(2);
        p1.setAwayScore(0);
        
        when(matchRepository.findByTournamentName("Liga")).thenReturn(Collections.singletonList(p1));

        List<Map<String, Object>> historial = estadisticasService.getTeamHistory("Liga", "Tigres");
        assertEquals(1, historial.size());
        assertEquals("WIN", historial.get(0).get("result"));
        assertEquals("HOME", historial.get(0).get("venue"));
    }

    @Test
    public void testGetHistorialEquipo_VisitanteDerrota() {
        MatchEntity p1 = new MatchEntity();
        p1.setHomeTeam("Tigres");
        p1.setAwayTeam("Leones");
        p1.setMatchDate("hoy");
        p1.setTournamentName("Liga");
        p1.setStatus("FINISHED");
        p1.setHomeScore(3);
        p1.setAwayScore(1);
        
        when(matchRepository.findByTournamentName("Liga")).thenReturn(Collections.singletonList(p1));

        List<Map<String, Object>> historial = estadisticasService.getTeamHistory("Liga", "Leones");
        assertEquals("LOSS", historial.get(0).get("result"));
        assertEquals("AWAY", historial.get(0).get("venue"));
    }
}
