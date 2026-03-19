package validator;

import dto.TorneoRequestDTO;
import model.Torneo;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TorneoValidatorTest {

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
    }

    @Test
    public void testValidateForCreation_Success() {
        TorneoRequestDTO req = new TorneoRequestDTO("Liga Beta", "2026-04-01", "2026-06-01", 16, 50.0);
        assertDoesNotThrow(() -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testValidateForCreation_NullNameThrows() {
        TorneoRequestDTO req = new TorneoRequestDTO(null, "2026-04-01", "2026-06-01", 16, 50.0);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testValidateForCreation_NegativeCostThrows() {
        TorneoRequestDTO req = new TorneoRequestDTO("Liga Beta", "2026-04-01", "2026-06-01", 16, -10.0);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }

    @Test
    public void testValidateForCreation_DuplicatedNameThrows() {
        Torneo t = new Torneo("Liga Beta");
        DataStorage.torneos.add(t);

        TorneoRequestDTO req = new TorneoRequestDTO("Liga Beta", "2026-05-01", "2026-07-01", 8, 20.0);
        assertThrows(IllegalArgumentException.class, () -> TorneoValidator.validateForCreation(req));
    }
}
