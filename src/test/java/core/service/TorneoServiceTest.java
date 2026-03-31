package core.service;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import dependencies.dto.TournamentHistoryDTO;
import core.validator.TournamentValidator;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TorneoServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentValidator tournamentValidator;

    @InjectMocks
    private TournamentService torneoService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateTorneo() {
        TournamentRequestDTO req = new TournamentRequestDTO("Liga Verano", "2026-06-01", "2026-08-01", 8, 100.0);
        
        doNothing().when(tournamentValidator).validateForCreation(req);
        when(tournamentRepository.findByTournamentName("Liga Verano")).thenReturn(Optional.empty());

        TournamentEntity savedEntity = new TournamentEntity();
        savedEntity.setTournamentName("Liga Verano");
        savedEntity.setStatus("DRAFT");
        savedEntity.setNumeroEquipos(8);
        savedEntity.setCostoInscripcion(100.0);
        
        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(savedEntity);

        TournamentResponseDTO res = torneoService.createTorneo(req);

        assertNotNull(res);
        assertEquals("Liga Verano", res.getTournamentName());
        assertEquals("DRAFT", res.getStatus());
        verify(tournamentRepository, times(1)).save(any(TournamentEntity.class));
    }

    @Test
    public void testConfigurarTorneo_Success() {
        String currId = "12345";
        TournamentEntity t = new TournamentEntity();
        t.setId(currId);
        t.setTournamentName("Liga Invierno");
        t.setStatus("DRAFT");

        when(tournamentRepository.findById(currId)).thenReturn(Optional.of(t));

        TournamentRequestDTO configInfo = new TournamentRequestDTO();
        configInfo.setRules("Reglas Oficiales");
        configInfo.setFechaCierreInscripciones("2026-05-30");
        configInfo.setFechaInicioFaseGrupos("2026-06-02");
        configInfo.setHorariosPartidos(Arrays.asList("18:00", "20:00"));
        configInfo.setCanchas(Arrays.asList("Cancha 1", "Cancha Central"));
        configInfo.setSanctions("Roja = 2 Fechas");

        TournamentEntity updatedEntity = new TournamentEntity();
        updatedEntity.setId(currId);
        updatedEntity.setTournamentName("Liga Invierno");
        updatedEntity.setStatus("DRAFT");
        updatedEntity.setRules("Reglas Oficiales");
        updatedEntity.setFechaCierreInscripciones("2026-05-30");
        updatedEntity.setHorariosPartidos(Arrays.asList("18:00", "20:00"));
        updatedEntity.setCanchas(Arrays.asList("Cancha 1", "Cancha Central"));

        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(updatedEntity);

        TournamentResponseDTO res = torneoService.configurarTorneo(currId, configInfo);

        assertNotNull(res);
        assertEquals("Reglas Oficiales", res.getRules());
        assertEquals("2026-05-30", res.getFechaCierreInscripciones());
        assertEquals(2, res.getHorariosPartidos().size());
        assertEquals("Cancha Central", res.getCanchas().get(1));
    }

    @Test
    public void testConfigurarTorneo_InvalidState_Throws() {
        String currId = "abcde";
        TournamentEntity t = new TournamentEntity();
        t.setId(currId);
        t.setTournamentName("Liga Bloqueada");
        t.setStatus("EN_PROGRESO");

        when(tournamentRepository.findById(currId)).thenReturn(Optional.of(t));

        TournamentRequestDTO configInfo = new TournamentRequestDTO();
        configInfo.setRules("Nuevas reglas");

        RuntimeException thrown = assertThrows(RuntimeException.class, 
            () -> torneoService.configurarTorneo(currId, configInfo));
        assertTrue(thrown.getMessage().contains("Solo se pueden configurar tournaments en status DRAFT o OPEN"));
    }

    @Test
    public void testGetAllTorneos() {
        TournamentEntity t1 = new TournamentEntity();
        t1.setTournamentName("Liga 1");
        
        TournamentEntity t2 = new TournamentEntity();
        t2.setTournamentName("Liga 2");

        List<TournamentEntity> entityList = new ArrayList<>();
        entityList.add(t1);
        entityList.add(t2);

        when(tournamentRepository.findAll()).thenReturn(entityList);

        List<TournamentHistoryDTO> res = torneoService.getAllTorneos();
        assertEquals(2, res.size());
    }
}
