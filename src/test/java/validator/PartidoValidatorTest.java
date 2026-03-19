package validator;

import dto.PartidoRequestDTO;
import model.Inscripcion;
import model.Torneo;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartidoValidatorTest {

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
    }

    @Test
    public void testValidateForCreation_Success() {
        DataStorage.torneos.add(new Torneo("Liga A"));
        
        Inscripcion i1 = new Inscripcion("A", "Liga A", "url");
        i1.setEstado("APROBADO");
        Inscripcion i2 = new Inscripcion("B", "Liga A", "url");
        i2.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);
        DataStorage.inscripciones.add(i2);

        PartidoRequestDTO req = new PartidoRequestDTO("A", "B", "2026-05-01", "Liga A");
        assertDoesNotThrow(() -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testValidateForCreation_SameTeamThrows() {
        PartidoRequestDTO req = new PartidoRequestDTO("A", "A", "2026-05-01", "Liga A");
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }

    @Test
    public void testValidateForCreation_NotApprovedThrows() {
        DataStorage.torneos.add(new Torneo("Liga A"));
        
        Inscripcion i1 = new Inscripcion("A", "Liga A", "url");
        i1.setEstado("PENDIENTE");
        Inscripcion i2 = new Inscripcion("B", "Liga A", "url");
        i2.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);
        DataStorage.inscripciones.add(i2);

        PartidoRequestDTO req = new PartidoRequestDTO("A", "B", "2026-05-01", "Liga A");
        assertThrows(IllegalArgumentException.class, () -> PartidoValidator.validateForCreation(req));
    }
}
