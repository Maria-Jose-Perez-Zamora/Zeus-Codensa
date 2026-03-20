package validator;

import dto.TeamRequestDTO;
import model.Team;
import model.Jugador;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TeamValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    private TeamRequestDTO validReq() {
        return new TeamRequestDTO("EqA", "E", "C", Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
    }

    @Test
    public void testValid() {
        assertDoesNotThrow(() -> TeamValidator.validateForCreation(validReq()));
    }

    @Test
    public void testNombreNullEmpty() {
        TeamRequestDTO req = validReq(); req.setNombreEquipo(null);
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(req));
        req.setNombreEquipo("  ");
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(req));
    }

    @Test
    public void testExists() {
        DataStorage.teams.add(new Team("EqA"));
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(validReq()));
    }

    @Test
    public void testJugadoresNull() {
        TeamRequestDTO req = validReq(); req.setJugadorCorreos(null);
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(req));
    }

    @Test
    public void testJugadoresCount() {
        TeamRequestDTO req = validReq(); req.setJugadorCorreos(Arrays.asList("1", "2"));
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(req));
        
        req.setJugadorCorreos(Collections.nCopies(21, "x"));
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(req));
    }

    @Test
    public void testJugadoresDuplicatedList() {
        TeamRequestDTO req = validReq(); req.setJugadorCorreos(Arrays.asList("1", "1", "3", "4", "5", "6", "7"));
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(req));
    }

    @Test
    public void testJugadorYaEnEquipo() {
        Team t = new Team("EqB");
        Jugador j = new Jugador(); j.setCorreo("1");
        t.getJugadores().add(j);
        DataStorage.teams.add(t);
        assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(validReq()));
    }
}
