package core.service;

import dependencias.dto.InscripcionRequestDTO;
import dependencias.dto.InscripcionResponseDTO;
import core.model.Team;
import core.model.Torneo;
import dependencias.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InscripcionServiceTest {

    private InscripcionService inscripcionService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        inscripcionService = new InscripcionService();
    }

    private void prepareTestEnv() {
        DataStorage.teams.add(new Team("Aguilas"));
        Torneo t = new Torneo("Nacional");
        t.setEstado("ABIERTO");
        DataStorage.torneos.add(t);
    }

    @Test
    public void testInscribir_Success() {
        prepareTestEnv();

        InscripcionRequestDTO req = new InscripcionRequestDTO("Aguilas", "Nacional", "url_pago");
        InscripcionResponseDTO res = inscripcionService.inscribir(req);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals("PENDIENTE", res.getEstado());
        assertEquals(1, DataStorage.inscripciones.size());
    }

    @Test
    public void testActualizarEstado_Success() {
        prepareTestEnv();

        InscripcionRequestDTO req = new InscripcionRequestDTO("Aguilas", "Nacional", "url_pago");
        InscripcionResponseDTO res = inscripcionService.inscribir(req);

        InscripcionResponseDTO updated = inscripcionService.actualizarEstado(res.getId(), "APROBADO");
        assertEquals("APROBADO", updated.getEstado());
    }

    @Test
    public void testActualizarEstado_InvalidState() {
        prepareTestEnv();

        InscripcionResponseDTO res = inscripcionService.inscribir(new InscripcionRequestDTO("Aguilas", "Nacional", "url_pago"));

        assertThrows(RuntimeException.class, () -> inscripcionService.actualizarEstado(res.getId(), "MISTERIO"));
    }

    @Test
    public void testActualizarEstado_IdNotFound_Throws() {
        assertThrows(RuntimeException.class, () -> inscripcionService.actualizarEstado("Inventado-123", "APROBADO"));
    }
}
