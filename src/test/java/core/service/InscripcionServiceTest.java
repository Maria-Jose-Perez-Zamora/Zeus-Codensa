package core.service;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import core.model.Team;
import core.model.Tournament;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InscripcionServiceTest {

    private RegistrationService inscripcionService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        inscripcionService = new RegistrationService();
    }

    private void prepareTestEnv() {
        DataStorage.teams.add(new Team("Aguilas"));
        Tournament t = new Tournament("Nacional");
        t.setStatus("OPEN");
        DataStorage.tournaments.add(t);
    }

    @Test
    public void testInscribir_Success() {
        prepareTestEnv();

        RegistrationRequestDTO req = new RegistrationRequestDTO("Aguilas", "Nacional", "url_pago");
        RegistrationResponseDTO res = inscripcionService.inscribir(req);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals("PENDING", res.getStatus());
        assertEquals(1, DataStorage.registrations.size());
    }

    @Test
    public void testActualizarEstado_Success() {
        prepareTestEnv();

        RegistrationRequestDTO req = new RegistrationRequestDTO("Aguilas", "Nacional", "url_pago");
        RegistrationResponseDTO res = inscripcionService.inscribir(req);

        inscripcionService.actualizarEstado(res.getId(), "IN_REVIEW");
        RegistrationResponseDTO updated = inscripcionService.actualizarEstado(res.getId(), "APPROVED");
        assertEquals("APPROVED", updated.getStatus());
    }

    @Test
    public void testActualizarEstado_InvalidState() {
        prepareTestEnv();

        RegistrationResponseDTO res = inscripcionService.inscribir(new RegistrationRequestDTO("Aguilas", "Nacional", "url_pago"));

        assertThrows(RuntimeException.class, () -> inscripcionService.actualizarEstado(res.getId(), "MISTERIO"));
    }

    @Test
    public void testActualizarEstado_IdNotFound_Throws() {
        assertThrows(RuntimeException.class, () -> inscripcionService.actualizarEstado("Inventado-123", "IN_REVIEW"));
    }
}
