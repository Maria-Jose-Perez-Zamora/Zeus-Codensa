package core.validator;

import dependencies.dto.RegistrationRequestDTO;
import core.model.Team;
import core.model.Tournament;
import core.model.Registration;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InscripcionValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    private RegistrationRequestDTO validReq() {
        return new RegistrationRequestDTO("EqA", "T1", "url");
    }

    private void setupValidEnv() {
        DataStorage.teams.add(new Team("EqA"));
        Tournament t = new Tournament("T1"); t.setStatus("OPEN");
        DataStorage.tournaments.add(t);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> RegistrationValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testNombreEquipoNullEmpty() {
        RegistrationRequestDTO req = validReq(); req.setNombreEquipo(null);
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(req));
        req.setNombreEquipo(" ");
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(req));
    }

    @Test
    public void testTorneoNullEmpty() {
        RegistrationRequestDTO req = validReq(); req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(req));
        req.setTournamentName(" ");
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(req));
    }

    @Test
    public void testComprobanteNullEmpty() {
        RegistrationRequestDTO req = validReq(); req.setComprobantePagoUrl(null);
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(req));
        req.setComprobantePagoUrl("");
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(req));
    }

    @Test
    public void testEquipoNoExiste() {
        Tournament t = new Tournament("T1"); t.setStatus("OPEN");
        DataStorage.tournaments.add(t);
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testTorneoNoExisteOEstadoDifferente() {
        DataStorage.teams.add(new Team("EqA"));
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(validReq()));

        Tournament t = new Tournament("T1"); t.setStatus("EN_PROGRESO");
        DataStorage.tournaments.add(t);
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testYaInscrito() {
        setupValidEnv();
        DataStorage.registrations.add(new Registration("EqA", "T1", "url"));
        assertThrows(IllegalArgumentException.class, () -> RegistrationValidator.validateForInscripcion(validReq()));
    }
}
