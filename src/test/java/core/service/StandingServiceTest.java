package core.service;

import core.model.Match;
import core.model.Standing;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StandingServiceTest {

    private StandingService tablaService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        tablaService = new StandingService();
    }

    @Test
    public void testCalcularTabla_Success() {
        Match p1 = new Match("Tigres", "Leones", "hoy", "Liga");
        p1.setHomeScore(2); p1.setAwayScore(1); p1.setStatus("FINISHED");
        
        Match p2 = new Match("Tigres", "Osos", "ayer", "Liga");
        p2.setHomeScore(1); p2.setAwayScore(1); p2.setStatus("FINISHED");
        
        DataStorage.matches.add(p1);
        DataStorage.matches.add(p2);

        List<Standing> standingTable = tablaService.calcularTabla("Liga");
        
        assertEquals(3, standingTable.size());
        assertEquals("Tigres", standingTable.get(0).getNombreEquipo()); // Tigres win (3) + draw (1) = 4 pts
        assertEquals(4, standingTable.get(0).getPoints());
        assertEquals(2, standingTable.get(0).getMatchesPlayed());
    }
}
