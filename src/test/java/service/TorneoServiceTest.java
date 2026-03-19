package service;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import model.Torneo;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertEquals("ABIERTO", res.getEstado());
        assertEquals(1, DataStorage.torneos.size());
    }

    @Test
    public void testGetAllTorneos() {
        DataStorage.torneos.add(new Torneo("Liga 1"));
        DataStorage.torneos.add(new Torneo("Liga 2"));

        List<TorneoResponseDTO> res = torneoService.getAllTorneos();
        assertEquals(2, res.size());
    }
}
