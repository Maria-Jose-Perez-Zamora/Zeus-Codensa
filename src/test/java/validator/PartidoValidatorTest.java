package validator;

import dto.PartidoRequestDTO;
import util.DataStorage;
import model.Torneo;
import model.Inscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PartidoValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    private PartidoRequestDTO validReq() {
        return new PartidoRequestDTO("EqA", "EqB", "2026", "T1");
    }

    private void setupValidEnv() {
        DataStorage.torneos.add(new Torneo("T1"));
        Inscripcion i1 = new Inscripcion("EqA", "T1", "url"); i1.setEstado("APROBADO");
        Inscripcion i2 = new Inscripcion("EqB", "T1", "url"); i2.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);
        DataStorage.inscripciones.add(i2);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> PartidoValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNull() {
        PartidoRequestDTO req = validReq(); req.setEquipoLocal(null);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testVisitanteNull() {
        PartidoRequestDTO req = validReq(); req.setEquipoVisitante(null);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testMismoEquipo() {
        PartidoRequestDTO req = validReq(); req.setEquipoLocal("EqA"); req.setEquipoVisitante("EqA");
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNull() {
        PartidoRequestDTO req = validReq(); req.setNombreTorneo(null);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testFechaNull() {
        PartidoRequestDTO req = validReq(); req.setFechaPartido(null);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNoExiste() {
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(validReq()));
    }

    @Test
    public void testVisitanteNoInscrito() {
        DataStorage.torneos.add(new Torneo("T1"));
        Inscripcion i1 = new Inscripcion("EqA", "T1", "url"); i1.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNoInscrito() {
        DataStorage.torneos.add(new Torneo("T1"));
        Inscripcion i2 = new Inscripcion("EqB", "T1", "url"); i2.setEstado("APROBADO");
        DataStorage.inscripciones.add(i2);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(validReq()));
    }

    @Test
    public void testEstadoNoAprobado() {
        DataStorage.torneos.add(new Torneo("T1"));
        Inscripcion i1 = new Inscripcion("EqA", "T1", "url"); i1.setEstado("PENDIENTE");
        Inscripcion i2 = new Inscripcion("EqB", "T1", "url"); i2.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);
        DataStorage.inscripciones.add(i2);
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(validReq()));
    }
}
