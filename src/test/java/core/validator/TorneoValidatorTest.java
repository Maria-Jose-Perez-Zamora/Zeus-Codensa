package core.validator;

import dependencias.dto.TorneoRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dependencias.util.DataStorage;
import core.model.Torneo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TorneoValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    @Test
    public void testValidCreation() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        assertDoesNotThrow(() -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testNullNombreTorneo() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo(null);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testEmptyNombreTorneo() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("   ");
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testNullEquipos() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Valido");
        req.setNumeroEquipos(null);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testInvalidEquipos() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Valido");
        req.setNumeroEquipos(1);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testNullCosto() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(null);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testNegativeCosto() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(-10.0);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testDuplicateTorneo() {
        Torneo t = new Torneo();
        t.setNombreTorneo("Valido");
        DataStorage.torneos.add(t);

        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }
}
