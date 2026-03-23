package core.service;

import core.model.Registration;
import core.model.KnockoutBracket;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BracketServiceTest {

    private BracketService llaveService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        llaveService = new BracketService();
    }

    @Test
    public void testGenerarLlaves_Success() {
        Registration i1 = new Registration("Eq1", "Liga", "url"); i1.setStatus("APROBADO");
        Registration i2 = new Registration("Eq2", "Liga", "url"); i2.setStatus("APROBADO");
        Registration i3 = new Registration("Eq3", "Liga", "url"); i3.setStatus("APROBADO");
        Registration i4 = new Registration("Eq4", "Liga", "url"); i4.setStatus("APROBADO");
        
        DataStorage.registrations.addAll(List.of(i1, i2, i3, i4));

        List<KnockoutBracket> llaves = llaveService.generarLlaves("Liga", "Cuartos");
        
        assertEquals(2, llaves.size());
        assertEquals("Cuartos", llaves.get(0).getPhase());
    }

    @Test
    public void testGenerarLlaves_OddNumberThrows() {
        Registration i1 = new Registration("Eq1", "Liga", "url"); i1.setStatus("APROBADO");
        DataStorage.registrations.add(i1);

        assertThrows(IllegalArgumentException.class, () -> llaveService.generarLlaves("Liga", "Cuartos"));
    }
}
