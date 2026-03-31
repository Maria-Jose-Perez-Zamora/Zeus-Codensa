package core.service;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import dependencies.dto.TournamentHistoryDTO;
import core.model.Tournament;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TorneoServiceTest {

    private TournamentService torneoService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        torneoService = new TournamentService();
    }

    @Test
    public void testCreateTorneo() {
        TournamentRequestDTO req = new TournamentRequestDTO("Liga Verano", "2026-06-01", "2026-08-01", 8, 100.0);
        TournamentResponseDTO res = torneoService.createTorneo(req);

        assertNotNull(res);
        assertEquals("Liga Verano", res.getTournamentName());
        assertEquals("DRAFT", res.getStatus());  // Default state from updated model
        assertEquals(1, DataStorage.tournaments.size());
    }

    @Test
    public void testConfigurarTorneo_Success() {
        // Prepare Borrador status Tournament
        DataStorage.tournaments.add(new Tournament("Liga Invierno"));
        String currId = DataStorage.tournaments.get(0).getId();

        TournamentRequestDTO configInfo = new TournamentRequestDTO();
        configInfo.setRules("Reglas Oficiales");
        configInfo.setFechaCierreInscripciones("2026-05-30");
        configInfo.setFechaInicioFaseGrupos("2026-06-02");
        configInfo.setHorariosPartidos(Arrays.asList("18:00", "20:00"));
        configInfo.setCanchas(Arrays.asList("Cancha 1", "Cancha Central"));
        configInfo.setSanctions("Roja = 2 Fechas");

        TournamentResponseDTO res = torneoService.configurarTorneo(currId, configInfo);

        assertNotNull(res);
        assertEquals("Reglas Oficiales", res.getRules());
        assertEquals("2026-05-30", res.getFechaCierreInscripciones());
        assertEquals(2, res.getHorariosPartidos().size());
        assertEquals("Cancha Central", res.getCanchas().get(1));
    }

    @Test
    public void testConfigurarTorneo_InvalidState_Throws() {
        Tournament t = new Tournament("Liga Bloqueada");
        t.setStatus("EN_PROGRESO");
        DataStorage.tournaments.add(t);

        TournamentRequestDTO configInfo = new TournamentRequestDTO();
        configInfo.setRules("Nuevas reglas");

        RuntimeException thrown = assertThrows(RuntimeException.class, 
            () -> torneoService.configurarTorneo(t.getId(), configInfo));
        assertTrue(thrown.getMessage().contains("Solo se pueden configurar tournaments en status DRAFT o OPEN"));
    }

    @Test
    public void testGetAllTorneos() {
        DataStorage.tournaments.add(new Tournament("Liga 1"));
        DataStorage.tournaments.add(new Tournament("Liga 2"));

        List<TournamentHistoryDTO> res = torneoService.getAllTorneos();
        assertEquals(2, res.size());
    }
}
