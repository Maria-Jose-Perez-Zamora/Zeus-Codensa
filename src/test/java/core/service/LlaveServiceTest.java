package core.service;

import core.model.Inscripcion;
import core.model.LlaveEliminatoria;
import dependencias.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LlaveServiceTest {

    private LlaveService llaveService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        llaveService = new LlaveService();
    }

    @Test
    public void testGenerarLlaves_Success() {
        Inscripcion i1 = new Inscripcion("Eq1", "Liga", "url"); i1.setEstado("APROBADO");
        Inscripcion i2 = new Inscripcion("Eq2", "Liga", "url"); i2.setEstado("APROBADO");
        Inscripcion i3 = new Inscripcion("Eq3", "Liga", "url"); i3.setEstado("APROBADO");
        Inscripcion i4 = new Inscripcion("Eq4", "Liga", "url"); i4.setEstado("APROBADO");
        
        DataStorage.inscripciones.addAll(List.of(i1, i2, i3, i4));

        List<LlaveEliminatoria> llaves = llaveService.generarLlaves("Liga", "Cuartos");
        
        assertEquals(2, llaves.size());
        assertEquals("Cuartos", llaves.get(0).getFase());
    }

    @Test
    public void testGenerarLlaves_OddNumberThrows() {
        Inscripcion i1 = new Inscripcion("Eq1", "Liga", "url"); i1.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);

        assertThrows(IllegalArgumentException.class, () -> llaveService.generarLlaves("Liga", "Cuartos"));
    }
}
