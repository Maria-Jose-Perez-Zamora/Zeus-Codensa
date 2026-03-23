package core.validator;

import dependencies.dto.TournamentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dependencies.util.DataStorage;
import core.model.Tournament;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TorneoValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    @Test
    public void testValidCreation() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        assertDoesNotThrow(() -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullNombreTorneo() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testEmptyNombreTorneo() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("   ");
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullEquipos() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(null);
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testInvalidEquipos() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(1);
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullCosto() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(null);
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNegativeCosto() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(-10.0);
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }

    @Test
    public void testDuplicateTorneo() {
        Tournament t = new Tournament();
        t.setTournamentName("Valido");
        DataStorage.tournaments.add(t);

        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        assertThrows(IllegalArgumentException.class, () -> TournamentValidator.validateForCreation(req));
    }
}
