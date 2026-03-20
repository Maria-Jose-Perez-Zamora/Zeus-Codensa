package validator;

import dto.InscripcionRequestDTO;
import model.Team;
import model.Torneo;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InscripcionValidatorTest {

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
    }

    private void prepareTestEnv() {
        DataStorage.teams.add(new Team("Tigres"));
        Torneo t = new Torneo("Liga A");
        t.setEstado("ABIERTO");
        DataStorage.torneos.add(t);
    }

    @Test
    public void testValidateForInscripcion_Success() {
        prepareTestEnv();

        InscripcionRequestDTO req = new InscripcionRequestDTO("Tigres", "Liga A", "http://pago.com");
        assertDoesNotThrow(() -> InscripcionValidator.validateForInscripcion(req));
    }

    @Test
    public void testValidateForInscripcion_TeamNotFound() {
        Torneo t = new Torneo("Liga A");
        t.setEstado("ABIERTO");
        DataStorage.torneos.add(t);

        InscripcionRequestDTO req = new InscripcionRequestDTO("Falso", "Liga A", "http://pago.com");
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
    }

    @Test
    public void testValidateForInscripcion_TorneoNotFound() {
        DataStorage.teams.add(new Team("Tigres"));

        InscripcionRequestDTO req = new InscripcionRequestDTO("Tigres", "Liga B", "http://pago.com");
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
    }
}
