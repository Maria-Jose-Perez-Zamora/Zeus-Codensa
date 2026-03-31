package core.service;

import core.model.Standing;
import dependencies.persistence.entity.MatchEntity;
import dependencies.persistence.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private MatchRepository matchRepository;

    @InjectMocks
    private StandingService tablaService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCalcularTabla_Success() {
        MatchEntity p1 = new MatchEntity();
        p1.setTournamentName("Liga");
        p1.setHomeTeam("Tigres");
        p1.setAwayTeam("Leones");
        p1.setHomeScore(2);
        p1.setAwayScore(1);
        p1.setStatus("FINISHED");
        p1.setMatchDate("hoy");
        
        MatchEntity p2 = new MatchEntity();
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
}
