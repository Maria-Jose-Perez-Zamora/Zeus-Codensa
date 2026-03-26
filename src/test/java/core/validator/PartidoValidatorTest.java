package core.validator;

import dependencies.dto.MatchRequestDTO;
import dependencies.util.DataStorage;
import core.model.Tournament;
import core.model.Registration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PartidoValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    private MatchRequestDTO validReq() {
        return new MatchRequestDTO("EqA", "EqB", "2026", "T1");
    }

    private void setupValidEnv() {
        DataStorage.tournaments.add(new Tournament("T1"));
        Registration i1 = new Registration("EqA", "T1", "url"); i1.setStatus("APPROVED");
        Registration i2 = new Registration("EqB", "T1", "url"); i2.setStatus("APPROVED");
        DataStorage.registrations.add(i1);
        DataStorage.registrations.add(i2);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> MatchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNull() {
        MatchRequestDTO req = validReq(); req.setHomeTeam(null);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(req));
    }

    @Test
    public void testVisitanteNull() {
        MatchRequestDTO req = validReq(); req.setAwayTeam(null);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(req));
    }

    @Test
    public void testMismoEquipo() {
        MatchRequestDTO req = validReq(); req.setHomeTeam("EqA"); req.setAwayTeam("EqA");
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNull() {
        MatchRequestDTO req = validReq(); req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(req));
    }

    @Test
    public void testFechaNull() {
        MatchRequestDTO req = validReq(); req.setMatchDate(null);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNoExiste() {
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testVisitanteNoInscrito() {
        DataStorage.tournaments.add(new Tournament("T1"));
        Registration i1 = new Registration("EqA", "T1", "url"); i1.setStatus("APPROVED");
        DataStorage.registrations.add(i1);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNoInscrito() {
        DataStorage.tournaments.add(new Tournament("T1"));
        Registration i2 = new Registration("EqB", "T1", "url"); i2.setStatus("APPROVED");
        DataStorage.registrations.add(i2);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testEstadoNoAprobado() {
        DataStorage.tournaments.add(new Tournament("T1"));
        Registration i1 = new Registration("EqA", "T1", "url"); i1.setStatus("PENDING");
        Registration i2 = new Registration("EqB", "T1", "url"); i2.setStatus("APPROVED");
        DataStorage.registrations.add(i1);
        DataStorage.registrations.add(i2);
        assertThrows(IllegalArgumentException.class, () -> MatchValidator.validateForCreation(validReq()));
    }
}
