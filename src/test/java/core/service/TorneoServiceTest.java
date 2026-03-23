package core.service;

import dependencias.dto.TorneoRequestDTO;
import dependencias.dto.TorneoResponseDTO;
import core.model.Torneo;
import dependencias.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TorneoServiceTest {

    private TorneoService torneoService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        torneoService = new TorneoService();
    }

    @Test
    public void testCreateTorneo() {
        TorneoRequestDTO req = new TorneoRequestDTO("Liga Verano", "2026-06-01", "2026-08-01", 8, 100.0);
        TorneoResponseDTO res = torneoService.createTorneo(req);

        assertNotNull(res);
        assertEquals("Liga Verano", res.getNombreTorneo());
        assertEquals("BORRADOR", res.getEstado());  // Default state from updated model
        assertEquals(1, DataStorage.torneos.size());
    }

    @Test
    public void testConfigurarTorneo_Success() {
        // Prepare Borrador status Torneo
        DataStorage.torneos.add(new Torneo("Liga Invierno"));
        String currId = DataStorage.torneos.get(0).getId();

        TorneoRequestDTO configInfo = new TorneoRequestDTO();
        configInfo.setReglamento("Reglas Oficiales");
        configInfo.setFechaCierreInscripciones("2026-05-30");
        configInfo.setFechaInicioFaseGrupos("2026-06-02");
        configInfo.setHorariosPartidos(Arrays.asList("18:00", "20:00"));
        configInfo.setCanchas(Arrays.asList("Cancha 1", "Cancha Central"));
        configInfo.setSanciones("Roja = 2 Fechas");

        TorneoResponseDTO res = torneoService.configurarTorneo(currId, configInfo);

        assertNotNull(res);
        assertEquals("Reglas Oficiales", res.getReglamento());
        assertEquals("2026-05-30", res.getFechaCierreInscripciones());
        assertEquals(2, res.getHorariosPartidos().size());
        assertEquals("Cancha Central", res.getCanchas().get(1));
    }

    @Test
    public void testConfigurarTorneo_InvalidState_Throws() {
        Torneo t = new Torneo("Liga Bloqueada");
        t.setEstado("EN_PROGRESO");
        DataStorage.torneos.add(t);

        TorneoRequestDTO configInfo = new TorneoRequestDTO();
        configInfo.setReglamento("Nuevas reglas");

        RuntimeException thrown = assertThrows(RuntimeException.class, 
            () -> torneoService.configurarTorneo(t.getId(), configInfo));
        assertTrue(thrown.getMessage().contains("Solo se pueden configurar torneos en estado BORRADOR o ABIERTO"));
    }

    @Test
    public void testGetAllTorneos() {
        DataStorage.torneos.add(new Torneo("Liga 1"));
        DataStorage.torneos.add(new Torneo("Liga 2"));

        List<TorneoResponseDTO> res = torneoService.getAllTorneos();
        assertEquals(2, res.size());
    }
}
