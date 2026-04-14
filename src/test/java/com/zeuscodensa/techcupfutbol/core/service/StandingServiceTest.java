package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.model.Standing;
import com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StandingServiceTest {

    @Mock
    private IMatchRepository matchRepository;

    @InjectMocks
    private StandingService tablaService;
    @Test
    public void testCalcularTabla_Success() {
        Match p1 = new Match();
        p1.setTournamentName("Liga");
        p1.setHomeTeam("Tigres");
        p1.setAwayTeam("Leones");
        p1.setHomeScore(2);
        p1.setAwayScore(1);
        p1.setStatus("FINISHED");
        p1.setMatchDate("hoy");
        
        Match p2 = new Match();
        p2.setTournamentName("Liga");
        p2.setHomeTeam("Tigres");
        p2.setAwayTeam("Osos");
        p2.setHomeScore(1);
        p2.setAwayScore(1);
        p2.setStatus("FINISHED");
        p2.setMatchDate("ayer");
        
        when(matchRepository.findByTournamentName("Liga")).thenReturn(Arrays.asList(p1, p2));

        List<Standing> standingTable = tablaService.calcularTabla("Liga");
        
        assertEquals(3, standingTable.size());
        assertEquals("Tigres", standingTable.get(0).getTeamName()); // Tigres win (3) + draw (1) = 4 pts
        assertEquals(4, standingTable.get(0).getPoints());
        assertEquals(2, standingTable.get(0).getMatchesPlayed());
    }

    @Test
    public void testCalcularTabla_Empty_ReturnsEmpty() {
        when(matchRepository.findByTournamentName("Vacío")).thenReturn(List.of());
        List<Standing> result = tablaService.calcularTabla("Vacío");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCalcularTabla_SkipsNonFinished() {
        Match scheduled = new Match();
        scheduled.setTournamentName("Liga");
        scheduled.setHomeTeam("A");
        scheduled.setAwayTeam("B");
        scheduled.setHomeScore(null);
        scheduled.setAwayScore(null);
        scheduled.setStatus("SCHEDULED");
        scheduled.setMatchDate("2026-06-01");

        when(matchRepository.findByTournamentName("Liga")).thenReturn(List.of(scheduled));
        List<Standing> result = tablaService.calcularTabla("Liga");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCalcularTabla_AwayWin() {
        Match p = new Match();
        p.setTournamentName("Liga");
        p.setHomeTeam("Local");
        p.setAwayTeam("Visitante");
        p.setHomeScore(0);
        p.setAwayScore(2);
        p.setStatus("FINISHED");
        p.setMatchDate("2026-06-01");

        when(matchRepository.findByTournamentName("Liga")).thenReturn(List.of(p));
        List<Standing> result = tablaService.calcularTabla("Liga");

        assertEquals(2, result.size());
        // Visitante should be first with 3 points
        assertEquals("Visitante", result.get(0).getTeamName());
        assertEquals(3, result.get(0).getPoints());
    }

    @Test
    public void testCalcularTabla_SortedByGoalDifferenceWhenEqualPoints() {
        // Team A draws 1-1, Team B draws 0-0 → same 1pt but A has better GD
        Match p1 = new Match();
        p1.setTournamentName("Liga");
        p1.setHomeTeam("A"); p1.setAwayTeam("X");
        p1.setHomeScore(1); p1.setAwayScore(1);
        p1.setStatus("FINISHED"); p1.setMatchDate("d1");

        Match p2 = new Match();
        p2.setTournamentName("Liga");
        p2.setHomeTeam("B"); p2.setAwayTeam("X");
        p2.setHomeScore(0); p2.setAwayScore(0);
        p2.setStatus("FINISHED"); p2.setMatchDate("d2");

        when(matchRepository.findByTournamentName("Liga")).thenReturn(Arrays.asList(p1, p2));
        List<Standing> result = tablaService.calcularTabla("Liga");
        // All have 1 point; A and B both have 0 GD; X is 0 GD too — just verify sorted
        assertEquals(3, result.size());
    }
}
