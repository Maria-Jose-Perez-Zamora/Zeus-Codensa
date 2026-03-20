package validator;

import dto.InscripcionRequestDTO;
import model.Team;
import model.Torneo;
import model.Inscripcion;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InscripcionValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    private InscripcionRequestDTO validReq() {
        return new InscripcionRequestDTO("EqA", "T1", "url");
    }

    private void setupValidEnv() {
        DataStorage.teams.add(new Team("EqA"));
        Torneo t = new Torneo("T1"); t.setEstado("ABIERTO");
        DataStorage.torneos.add(t);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> InscripcionValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testNombreEquipoNullEmpty() {
        InscripcionRequestDTO req = validReq(); req.setNombreEquipo(null);
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
        req.setNombreEquipo(" ");
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
    }

    @Test
    public void testTorneoNullEmpty() {
        InscripcionRequestDTO req = validReq(); req.setNombreTorneo(null);
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
        req.setNombreTorneo(" ");
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
    }

    @Test
    public void testComprobanteNullEmpty() {
        InscripcionRequestDTO req = validReq(); req.setComprobantePagoUrl(null);
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
        req.setComprobantePagoUrl("");
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(req));
    }

    @Test
    public void testEquipoNoExiste() {
        Torneo t = new Torneo("T1"); t.setEstado("ABIERTO");
        DataStorage.torneos.add(t);
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testTorneoNoExisteOEstadoDifferente() {
        DataStorage.teams.add(new Team("EqA"));
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(validReq()));

        Torneo t = new Torneo("T1"); t.setEstado("EN_PROGRESO");
        DataStorage.torneos.add(t);
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testYaInscrito() {
        setupValidEnv();
        DataStorage.inscripciones.add(new Inscripcion("EqA", "T1", "url"));
        assertThrows(IllegalArgumentException.class, () -> InscripcionValidator.validateForInscripcion(validReq()));
    }
}
